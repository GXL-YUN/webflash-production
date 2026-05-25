package cn.enilu.flash.bean.core;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 筛选参数对象
 */
public class FilterParam {

    /**
     * 筛选字段名
     */
    @JsonProperty("key")
    private String field;

    /**
     * 筛选值
     * 可以是单个值（String, Number, Boolean）
     * 也可以是数组（List）
     */
    @JsonProperty("value")
    private Object value;

    /**
     * 筛选类型
     * eq, ne, gt, lt, ge, le, like, in, notIn, between, isNull, isNotNull
     */
    @JsonProperty("type")
    private String type = "eq";

    /**
     * 连接条件（暂时保留，用于未来扩展）
     */
    @JsonProperty("logic")
    private String logic = "and";

    public FilterParam() {}

    public FilterParam(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public FilterParam(String field, Object value, String type) {
        this.field = field;
        this.value = value;
        this.type = type;
    }

    // Getters and Setters
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLogic() { return logic; }
    public void setLogic(String logic) { this.logic = logic; }

    @Override
    public String toString() {
        return "FilterParam{" +
                "field='" + field + '\'' +
                ", value=" + value +
                ", type='" + type + '\'' +
                ", logic='" + logic + '\'' +
                '}';
    }
}
