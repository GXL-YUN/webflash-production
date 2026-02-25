package cn.enilu.flash.api.utils.sql.util;


import cn.enilu.flash.api.utils.sql.bean.Alias;
import cn.enilu.flash.api.utils.sql.bean.BaseEntity;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SQL生成工具类
 */
public class SqlGenerator {

    /**
     * 生成插入SQL语句
     * 只包含有值的字段
     */
    public static <T extends BaseEntity> String generateInsertSql(T entity) {
        Class<?> clazz = entity.getClass();
        String tableName = entity.getTableName();

        // 获取有值的字段
        Map<String, Object> fieldMap = getNotNullFieldsWithAlias(entity);

        if (fieldMap.isEmpty()) {
            throw new IllegalArgumentException("没有有效字段可插入");
        }

        // 构建列名和值占位符
        String columns = String.join(", ", fieldMap.keySet());
        String placeholders = fieldMap.keySet().stream()
                .map(col -> ":" + toCamelCase(col))
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, columns, placeholders);
    }

    /**
     * 生成更新SQL语句
     * 只更新有值的字段
     */
    public static <T extends BaseEntity> String generateUpdateSql(T entity) {
        Class<?> clazz = entity.getClass();
        String tableName = entity.getTableName();
        String primaryKey = entity.getPrimaryKey();

        // 获取有值的字段（排除主键）
        Map<String, Object> fieldMap = getNotNullFieldsWithAlias(entity);
        fieldMap.remove(primaryKey);

        if (fieldMap.isEmpty()) {
            throw new IllegalArgumentException("没有有效字段可更新");
        }

        // 构建SET子句
        String setClause = fieldMap.keySet().stream()
                .map(col -> col + " = :" + toCamelCase(col))
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s = :%s",
                tableName, setClause,
                primaryKey, toCamelCase(primaryKey));
    }

    /**
     * 生成删除SQL语句
     */
    public static <T extends BaseEntity> String generateDeleteSql(T entity) {
        String tableName = entity.getTableName();
        String primaryKey = entity.getPrimaryKey();

        return String.format("DELETE FROM %s WHERE %s = :%s",
                tableName, primaryKey, toCamelCase(primaryKey));
    }

    /**
     * 生成查询SQL语句
     * @param includeNullFields 是否包含空值字段作为查询条件
     */
    public static <T extends BaseEntity> String generateSelectSql(T entity, boolean includeNullFields) {
        String tableName = entity.getTableName();
        Map<String, Object> fieldMap = includeNullFields ?
                getAllFieldsWithAlias(entity) : getNotNullFieldsWithAlias(entity);

        if (fieldMap.isEmpty()) {
            return String.format("SELECT * FROM %s", tableName);
        }

        String whereClause = fieldMap.keySet().stream()
                .map(col -> col + " = :" + toCamelCase(col))
                .collect(Collectors.joining(" AND "));

        return String.format("SELECT * FROM %s WHERE %s", tableName, whereClause);
    }

    /**
     * 获取有值的字段及其别名
     */
    static <T extends BaseEntity> Map<String, Object> getNotNullFieldsWithAlias(T entity) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(entity);

                // 只处理非空值
                if (value != null) {
                    Alias alias = field.getAnnotation(Alias.class);
                    if (alias != null) {
                        result.put(alias.value(), value);
                    } else {
                        // 如果没有@Alias注解，使用字段名
                        result.put(field.getName(), value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取字段值失败", e);
        }
        return result;
    }

    /**
     * 获取所有字段及其别名
     */
    private static <T extends BaseEntity> Map<String, Object> getAllFieldsWithAlias(T entity) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(entity);

                Alias alias = field.getAnnotation(Alias.class);
                if (alias != null) {
                    result.put(alias.value(), value);
                } else {
                    result.put(field.getName(), value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取字段值失败", e);
        }
        return result;
    }

    /**
     * 转换为驼峰命名（用于命名参数）
     */
    public static String toCamelCase(String column) {
        if (column == null || column.isEmpty()) {
            return column;
        }

        // 移除下划线并转换为驼峰
        String[] parts = column.split("_");
        StringBuilder result = new StringBuilder(parts[0].toLowerCase());

        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                result.append(Character.toUpperCase(parts[i].charAt(0)))
                        .append(parts[i].substring(1).toLowerCase());
            }
        }

        return result.toString();
    }
}