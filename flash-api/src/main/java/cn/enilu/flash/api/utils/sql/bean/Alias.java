package cn.enilu.flash.api.utils.sql.bean;

import java.lang.annotation.*;

/**
 * 字段别名注解，用于数据库字段映射
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Alias {
    /**
     * 数据库字段名
     */
    String value();

    /**
     * 是否为主键
     */
    boolean isPrimaryKey() default false;

    /**
     * 插入时是否必填
     */
    boolean required() default false;

    /**
     * 更新时间自动填充
     */
    boolean autoUpdate() default false;

    /**
     * 创建时间自动填充
     */
    boolean autoCreate() default false;
}