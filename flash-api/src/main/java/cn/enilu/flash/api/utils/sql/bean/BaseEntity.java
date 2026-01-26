package cn.enilu.flash.api.utils.sql.bean;

import java.lang.reflect.Field;

/**
 * 实体基类
 */
public abstract class BaseEntity {
    /**
     * 获取表名
     */
    public String getTableName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * 获取主键字段名
     */
    public String getPrimaryKey() {
        for (Field field : this.getClass().getDeclaredFields()) {
            Alias alias = field.getAnnotation(Alias.class);
            if (alias != null && alias.isPrimaryKey()) {
                return alias.value();
            }
        }
        return "id"; // 默认主键字段
    }
}
