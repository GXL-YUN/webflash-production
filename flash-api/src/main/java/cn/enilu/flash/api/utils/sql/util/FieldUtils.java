package cn.enilu.flash.api.utils.sql.util;


import cn.enilu.flash.api.utils.sql.bean.Alias;
import cn.enilu.flash.api.utils.sql.bean.BaseEntity;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldUtils {

    /**
     * 获取非空字段（支持驼峰命名转换）
     */
    public static <T extends BaseEntity> Map<String, Object> getNotNullFieldsWithAlias(T entity) {
        return getNotNullFieldsWithAlias(entity, true);
    }

    /**
     * 获取非空字段（可选择是否转换为驼峰命名）
     */
    public static <T extends BaseEntity> Map<String, Object> getNotNullFieldsWithAlias(
            T entity, boolean convertToCamelCase) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(entity);

                // 只处理非空值
                if (value != null) {
                    String columnName = getColumnName(field, convertToCamelCase);
                    result.put(columnName, value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取字段值失败", e);
        }
        return result;
    }

    /**
     * 获取字段对应的列名（支持驼峰转换）
     */
    private static String getColumnName(Field field, boolean convertToCamelCase) {
        Alias alias = field.getAnnotation(Alias.class);
        if (alias != null) {
            // 如果有@Alias注解，使用注解值
            return convertToCamelCase ? toCamelCase(alias.value()) : alias.value();
        } else {
            // 如果没有@Alias注解，使用字段名
            return convertToCamelCase ? toCamelCase(field.getName()) : field.getName();
        }
    }

    /**
     * 下划线命名转驼峰命名
     */
    public static String toCamelCase(String columnName) {
        if (columnName == null || columnName.isEmpty()) {
            return columnName;
        }

        // 如果已经是驼峰命名，直接返回
        if (!columnName.contains("_")) {
            return columnName;
        }

        String[] parts = columnName.split("_");
        StringBuilder result = new StringBuilder(parts[0].toLowerCase());

        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                result.append(Character.toUpperCase(parts[i].charAt(0)))
                        .append(parts[i].substring(1).toLowerCase());
            }
        }

        return result.toString();
    }

    /**
     * 驼峰命名转下划线命名
     */
    public static String toSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 获取所有字段（包括空值）
     */
    public static <T extends BaseEntity> Map<String, Object> getAllFieldsWithAlias(
            T entity, boolean convertToCamelCase) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(entity);
                String columnName = getColumnName(field, convertToCamelCase);
                result.put(columnName, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取字段值失败", e);
        }
        return result;
    }

    /**
     * 获取字段名和列名的映射关系
     */
    public static <T extends BaseEntity> Map<String, String> getFieldColumnMapping(
            Class<T> clazz, boolean convertToCamelCase) {
        Map<String, String> mapping = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            String columnName = getColumnName(field, convertToCamelCase);
            mapping.put(field.getName(), columnName);
        }
        return mapping;
    }
}