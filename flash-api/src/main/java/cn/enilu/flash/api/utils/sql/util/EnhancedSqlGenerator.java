package cn.enilu.flash.api.utils.sql.util;

import cn.enilu.flash.api.utils.sql.bean.Alias;
import cn.enilu.flash.api.utils.sql.bean.BaseEntity;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 增强版SQL生成器
 */
public class EnhancedSqlGenerator {

    /**
     * 生成插入SQL（带自动填充）
     */
    public static <T extends BaseEntity> String generateInsertSqlWithAutoFill(T entity) {
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);

        List<FieldInfo> fieldInfos = getFieldInfos(clazz, entity, true);
        List<FieldInfo> insertFields = new ArrayList<>();

        for (FieldInfo fieldInfo : fieldInfos) {
            // 跳过自增主键
            if (fieldInfo.isAutoIncrement()) {
                continue;
            }

            // 自动填充创建时间
            if (fieldInfo.isAutoCreate() && fieldInfo.getValue() == null) {
                fieldInfo.setValue(LocalDateTime.now());
            }

            // 有值或必填的字段
            if (fieldInfo.getValue() != null || fieldInfo.isRequired()) {
                insertFields.add(fieldInfo);
            }
        }

        if (insertFields.isEmpty()) {
            throw new IllegalArgumentException("没有有效字段可插入");
        }

        String columns = insertFields.stream()
                .map(FieldInfo::getColumnName)
                .collect(Collectors.joining(", "));

        String placeholders = insertFields.stream()
                .map(field -> ":" + field.getFieldName())
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, columns, placeholders);
    }

    /**
     * 生成更新SQL（带自动填充和乐观锁）
     */
    public static <T extends BaseEntity> String generateUpdateSqlWithAutoFill(T entity) {
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);

        List<FieldInfo> fieldInfos = getFieldInfos(clazz, entity, false);
        String primaryKey = getPrimaryKey(fieldInfos);
        String versionColumn = getVersionColumn(fieldInfos);

        // 过滤出需要更新的字段
        List<String> setClauses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        for (FieldInfo fieldInfo : fieldInfos) {
            if (fieldInfo.getColumnName().equals(primaryKey) ||
                    fieldInfo.isAutoIncrement()) {
                continue;
            }

            // 自动填充更新时间
            if (fieldInfo.isAutoUpdate()) {
                fieldInfo.setValue(LocalDateTime.now());
            }

            // 只更新有值的字段
            if (fieldInfo.getValue() != null) {
                setClauses.add(fieldInfo.getColumnName() + " = :" + fieldInfo.getFieldName());
                params.put(fieldInfo.getFieldName(), fieldInfo.getValue());
            }
        }

        if (setClauses.isEmpty()) {
            throw new IllegalArgumentException("没有有效字段可更新");
        }

        // 添加乐观锁条件
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName)
                .append(" SET ").append(String.join(", ", setClauses))
                .append(" WHERE ").append(primaryKey).append(" = :").append(toCamelCase(primaryKey));

        if (versionColumn != null) {
            sql.append(" AND ").append(versionColumn).append(" = :").append(toCamelCase(versionColumn));
        }

        return sql.toString();
    }

    /**
     * 生成批量插入SQL
     */
    public static <T extends BaseEntity> String generateBatchInsertSql(List<T> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            throw new IllegalArgumentException("实体列表不能为空");
        }

        T first = entities.get(0);
        Class<?> clazz = first.getClass();
        String tableName = getTableName(clazz);

        // 获取所有实体的共有字段
        Set<String> allColumns = new LinkedHashSet<>();
        List<Map<String, Object>> allValues = new ArrayList<>();

        for (T entity : entities) {
            Map<String, Object> fieldMap = getNotNullFieldsWithAlias(entity);
            allColumns.addAll(fieldMap.keySet());
            allValues.add(fieldMap);
        }

        String columns = String.join(", ", allColumns);

        // 构建VALUES子句
        StringBuilder valuesClause = new StringBuilder();
        for (int i = 0; i < allValues.size(); i++) {
            if (i > 0) valuesClause.append(", ");
            valuesClause.append("(");

            boolean firstCol = true;
            for (String column : allColumns) {
                if (!firstCol) valuesClause.append(", ");
                Map<String, Object> row = allValues.get(i);
                valuesClause.append(":").append(toCamelCase(column)).append(i);
                firstCol = false;
            }

            valuesClause.append(")");
        }

        return String.format("INSERT INTO %s (%s) VALUES %s",
                tableName, columns, valuesClause);
    }

    /**
     * 生成批量更新SQL
     */
    public static <T extends BaseEntity> String generateBatchUpdateSql(List<T> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            throw new IllegalArgumentException("实体列表不能为空");
        }

        T first = entities.get(0);
        String tableName = getTableName(first.getClass());
        String primaryKey = getPrimaryKey(getFieldInfos(first.getClass(), first, false));

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName).append(" SET\n");

        // 获取需要更新的字段
        Set<String> updateColumns = new HashSet<>();
        for (T entity : entities) {
            Map<String, Object> fieldMap = getNotNullFieldsWithAlias(entity);
            fieldMap.remove(primaryKey);
            updateColumns.addAll(fieldMap.keySet());
        }

        // 构建CASE WHEN语句
        for (String column : updateColumns) {
            sql.append("  ").append(column).append(" = CASE ").append(primaryKey).append("\n");
            for (int i = 0; i < entities.size(); i++) {
                T entity = entities.get(i);
                Map<String, Object> fieldMap = getNotNullFieldsWithAlias(entity);
                Object value = fieldMap.get(column);
                Object pkValue = getFieldValue(entity, primaryKey);

                if (value != null && pkValue != null) {
                    sql.append("    WHEN :pk").append(i).append(" THEN :").append(toCamelCase(column)).append(i).append("\n");
                }
            }
            sql.append("    ELSE ").append(column).append("\n");
            sql.append("  END,\n");
        }

        // 移除最后一个逗号
        sql.deleteCharAt(sql.length() - 2);

        // 添加WHERE条件
        sql.append("WHERE ").append(primaryKey).append(" IN (");
        for (int i = 0; i < entities.size(); i++) {
            if (i > 0) sql.append(", ");
            sql.append(":pk").append(i);
        }
        sql.append(")");

        return sql.toString();
    }

    /**
     * 字段信息封装类
     */
    private static class FieldInfo {
        private String fieldName;
        private String columnName;
        private Object value;
        private boolean isPrimaryKey;
        private boolean isAutoIncrement;
        private boolean required;
        private boolean autoCreate;
        private boolean autoUpdate;

        // getters and setters
        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        public String getColumnName() { return columnName; }
        public void setColumnName(String columnName) { this.columnName = columnName; }
        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
        public boolean isPrimaryKey() { return isPrimaryKey; }
        public void setPrimaryKey(boolean primaryKey) { isPrimaryKey = primaryKey; }
        public boolean isAutoIncrement() { return isAutoIncrement; }
        public void setAutoIncrement(boolean autoIncrement) { isAutoIncrement = autoIncrement; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public boolean isAutoCreate() { return autoCreate; }
        public void setAutoCreate(boolean autoCreate) { this.autoCreate = autoCreate; }
        public boolean isAutoUpdate() { return autoUpdate; }
        public void setAutoUpdate(boolean autoUpdate) { this.autoUpdate = autoUpdate; }
    }

    /**
     * 获取字段信息
     */
    private static List<FieldInfo> getFieldInfos(Class<?> clazz, Object entity, boolean forInsert) {
        List<FieldInfo> fieldInfos = new ArrayList<>();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(field.getName());
                fieldInfo.setValue(field.get(entity));

                Alias alias = field.getAnnotation(Alias.class);
                if (alias != null) {
                    fieldInfo.setColumnName(alias.value());
                    fieldInfo.setPrimaryKey(alias.isPrimaryKey());
                    fieldInfo.setRequired(alias.required());
                    fieldInfo.setAutoCreate(alias.autoCreate());
                    fieldInfo.setAutoUpdate(alias.autoUpdate());
                } else {
                    fieldInfo.setColumnName(field.getName());
                }

                // 判断是否自增
                if (forInsert && field.getType() == Long.class &&
                        field.getName().toLowerCase().contains("id")) {
                    fieldInfo.setAutoIncrement(true);
                }

                fieldInfos.add(fieldInfo);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取字段信息失败", e);
        }
        return fieldInfos;
    }

    private static String getTableName(Class<?> clazz) {
        try {
            Method method = clazz.getMethod("getTableName");
            return (String) method.invoke(clazz.newInstance());
        } catch (Exception e) {
            return clazz.getSimpleName().toLowerCase();
        }
    }

    private static String getPrimaryKey(List<FieldInfo> fieldInfos) {
        return fieldInfos.stream()
                .filter(FieldInfo::isPrimaryKey)
                .map(FieldInfo::getColumnName)
                .findFirst()
                .orElse("id");
    }

    private static String getVersionColumn(List<FieldInfo> fieldInfos) {
        return fieldInfos.stream()
                .filter(field -> field.getColumnName().equalsIgnoreCase("version"))
                .map(FieldInfo::getColumnName)
                .findFirst()
                .orElse(null);
    }

    private static Object getFieldValue(Object entity, String fieldName) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            return null;
        }
    }

    private static String toCamelCase(String column) {
        // 实现同前
        return SqlGenerator.toCamelCase(column);
    }

    private static Map<String, Object> getNotNullFieldsWithAlias(Object entity) {
        // 实现同前
        return SqlGenerator.getNotNullFieldsWithAlias((BaseEntity) entity);
    }
}