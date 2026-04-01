package cn.enilu.util;



import cn.enilu.flash.bean.requert.FilterParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SQL条件生成器
 */
public class SqlConditionBuilderUtil {

    /**
     * 操作符枚举
     */
    public enum Operator {
        EQ("eq", "=", "等于"),
        NE("ne", "!=", "不等于"),
        GT("gt", ">", "大于"),
        LT("lt", "<", "小于"),
        GE("ge", ">=", "大于等于"),
        LE("le", "<=", "小于等于"),
        LIKE("like", "LIKE", "模糊匹配"),
        NOT_LIKE("notLike", "NOT LIKE", "不匹配"),
        IN("in", "IN", "包含"),
        NOT_IN("notIn", "NOT IN", "不包含"),
        BETWEEN("between", "BETWEEN", "介于"),
        BETWEENTIME("betweenTime", "BETWEENTIME", "时间介于"),
        IS_NULL("isNull", "IS NULL", "为空"),
        IS_NOT_NULL("isNotNull", "IS NOT NULL", "不为空");

        private final String code;
        private final String symbol;
        private final String description;

        Operator(String code, String symbol, String description) {
            this.code = code;
            this.symbol = symbol;
            this.description = description;
        }

        public String getCode() { return code; }
        public String getSymbol() { return symbol; }
        public String getDescription() { return description; }

        private static final Map<String, Operator> CODE_MAP = Arrays.stream(Operator.values())
                .collect(Collectors.toMap(Operator::getCode, op -> op));

        public static Operator fromCode(String code) {
            return CODE_MAP.getOrDefault(code, EQ);
        }
    }

    /**
     * 构建完整的SQL WHERE子句
     * @param params 筛选参数列表
     * @return WHERE子句字符串
     */
    public static String buildWhereClause(List<FilterParam> params) {
        if (params == null || params.isEmpty()) {
            return "1=1";
        }

        List<String> conditions = new ArrayList<>();

        for (FilterParam param : params) {
            if(param.getValue() instanceof ArrayList<?>){
                if(((ArrayList<?>) param.getValue()).size()>0){
                    String condition = buildCondition(param);
                    if (condition != null && !condition.trim().isEmpty()) {
                        conditions.add(condition);
                    }
                }
            } else if (param.getValue() instanceof String) {

                if(StringUtil.isNotNull((String) param.getValue())){
                    String condition = buildCondition(param);
                    if (condition != null && !condition.trim().isEmpty()) {
                        conditions.add(condition);
                    }
                }
            }

        }

        if (conditions.isEmpty()) {
            return "1=1";
        }

        return String.join(" AND ", conditions);
    }

    /**
     * 构建单个条件的SQL
     */
    private static String buildCondition(FilterParam param) {
        String field = param.getField();
        Object value = param.getValue();
        String type = param.getType();

        if (field == null || field.trim().isEmpty()) {
            return null;
        }

        Operator operator = Operator.fromCode(type);

        try {
            switch (operator) {
                case EQ:
                    return buildEqualsCondition(field, value);
                case NE:
                    return buildNotEqualsCondition(field, value);
                case GT:
                case LT:
                case GE:
                case LE:
                    return buildCompareCondition(field, value, operator);
                case LIKE:
                case NOT_LIKE:
                    return buildLikeCondition(field, value, operator);
                case IN:
                case NOT_IN:
                    return buildInCondition(field, value, operator);
                case BETWEEN:
                    return buildBetweenCondition(field, value);

                case BETWEENTIME:
                    return buildBetweenConditionTime(field, value);
                case IS_NULL:
                    return field + " IS NULL";
                case IS_NOT_NULL:
                    return field + " IS NOT NULL";
                default:
                    return buildEqualsCondition(field, value);
            }
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("构建SQL条件失败: " + param + ", 错误: " + e.getMessage());
            return null;
        }
    }

    /**
     * 构建等于条件
     */
    private static String buildEqualsCondition(String field, Object value) {
        if (value == null) {
            return field + " IS NULL";
        }
        return field + " = " + formatValue(value);
    }

    /**
     * 构建不等于条件
     */
    private static String buildNotEqualsCondition(String field, Object value) {
        if (value == null) {
            return field + " IS NOT NULL";
        }
        return field + " != " + formatValue(value);
    }

    /**
     * 构建比较条件
     */
    private static String buildCompareCondition(String field, Object value, Operator operator) {
        if (value == null) {
            throw new IllegalArgumentException("比较操作符的值不能为空");
        }
        return field + " " + operator.getSymbol() + " " + formatValue(value);
    }

    /**
     * 构建模糊查询条件
     */
    private static String buildLikeCondition(String field, Object value, Operator operator) {
        if (value == null || value.toString().trim().isEmpty()) {
            return null;
        }
        String likeValue = "%" + escapeSql(value.toString()) + "%";
        return field + " " + operator.getSymbol() + " '" + likeValue + "'";
    }

    /**
     * 构建IN条件
     */
    private static String buildInCondition(String field, Object value, Operator operator) {
        if (value == null) {
            return null;
        }

        List<String> values = new ArrayList<>();

        if (value instanceof List) {
            List<?> list = (List<?>) value;
            for (Object item : list) {
                if (item != null) {
                    values.add(formatValue(item));
                }
            }
        } else if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            for (Object item : array) {
                if (item != null) {
                    values.add(formatValue(item));
                }
            }
        } else {
            // 单个值转为列表
            values.add(formatValue(value));
        }

        if (values.isEmpty()) {
            return operator == Operator.IN ? "1=0" : "1=1"; // IN 空列表永远为假
        }

        String valuesStr = String.join(", ", values);
        return field + " " + operator.getSymbol() + " (" + valuesStr + ")";
    }

    /**
     * 构建BETWEEN条件
     */
    private static String buildBetweenCondition(String field, Object value) {
        if (value == null) {
            return null;
        }

        List<Object> range = new ArrayList<>();
        if (value instanceof List) {
            range = (List<Object>) value;
        } else if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            range = Arrays.asList(array);
        } else {
            throw new IllegalArgumentException("BETWEEN操作符的值必须是数组或列表");
        }

        if (range.size() < 2) {
            throw new IllegalArgumentException("BETWEEN操作符需要两个值（起始和结束）");
        }

        Object start = range.get(0);
        Object end = range.get(1);

        if (start == null || end == null) {
            throw new IllegalArgumentException("BETWEEN操作符的值不能为空");
        }

        return field + " BETWEEN " + formatValue(start) + " AND " + formatValue(end);
    }


    /**
     * 时间介于
     * @param field
     * @param value
     * @return
     */
    private static String buildBetweenConditionTime(String field, Object value) {
        if (value == null) {
            return null;
        }

        List<Object> range = new ArrayList<>();
        if (value instanceof List) {
            range = (List<Object>) value;
        } else if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            range = Arrays.asList(array);
        } else {
            throw new IllegalArgumentException("BETWEEN操作符的值必须是数组或列表");
        }

        if (range.size() < 2) {
            throw new IllegalArgumentException("BETWEEN操作符需要两个值（起始和结束）");
        }

        Object start = range.get(0);
        Object end = range.get(1);

        if (start == null || end == null) {
            throw new IllegalArgumentException("BETWEEN操作符的值不能为空");
        }

        return field + " BETWEEN  DATE  " + formatValue(start) + " AND  DATE " + formatValue(end);
    }


    /**
     * 格式化值
     */
    private static String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        }

        if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? "1" : "0";
        } else if (value instanceof String) {
            return "'" + escapeSql(value.toString()) + "'";
        } else {
            // 处理其他类型
            return "'" + escapeSql(value.toString()) + "'";
        }
    }

    /**
     * SQL注入防护
     */
    private static String escapeSql(String value) {
        if (value == null) return "";
        return value.replace("'", "''");
    }

    /**
     * 构建完整的SQL查询
     */
    public static String buildSelectSql( List<FilterParam> params) {
//        String columnList = (columns == null || columns.length == 0)
//                ? "*"
//                : String.join(", ", columns);

        String whereClause = buildWhereClause(params);

        return
                whereClause;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试数据
        List<FilterParam> params = new ArrayList<>();

        // 示例1: IN 条件
        params.add(new FilterParam("fdname", Arrays.asList("D", "F"), "in"));
        params.add(new FilterParam("fdname1", Arrays.asList("7", "8", "10"), "in"));
        params.add(new FilterParam("fdname2", Arrays.asList("1"), "in"));

       // String sql = buildSelectSql("your_table", params, "id", "name", "status");
        System.out.println("示例1 - IN条件:");
       //System.out.println(sql);
        // 输出: SELECT id, name, status FROM your_table WHERE fdname IN ('D', 'F') AND fdname1 IN ('7', '8', '10') AND fdname2 IN ('1')

        // 示例2: 混合条件
        List<FilterParam> mixedParams = new ArrayList<>();
        mixedParams.add(new FilterParam("age", 18, "ge"));
        mixedParams.add(new FilterParam("age", 60, "le"));
        mixedParams.add(new FilterParam("name", "张", "like"));
        mixedParams.add(new FilterParam("status", Arrays.asList("active", "pending"), "in"));
        mixedParams.add(new FilterParam("deleted", false, "eq"));
        mixedParams.add(new FilterParam("createTime", Arrays.asList("2024-01-01", "2024-12-31"), "between"));

        //String mixedSql = buildSelectSql("users", mixedParams);
        System.out.println("\n示例2 - 混合条件:");
       // System.out.println(mixedSql);
        // 输出: SELECT * FROM users WHERE age >= 18 AND age <= 60 AND name LIKE '%张%' AND status IN ('active', 'pending') AND deleted = 0 AND createTime BETWEEN '2024-01-01' AND '2024-12-31'

        // 示例3: 空值和特殊值处理
        List<FilterParam> specialParams = new ArrayList<>();
        specialParams.add(new FilterParam("description", null, "isNull"));
        specialParams.add(new FilterParam("title", "", "isNotNull"));
        specialParams.add(new FilterParam("category", Arrays.asList(), "in")); // 空列表

        //tring specialSql = buildSelectSql("articles", specialParams);
        System.out.println("\n示例3 - 空值和特殊值:");
       // System.out.println(specialSql);
        // 输出: SELECT * FROM articles WHERE description IS NULL AND title IS NOT NULL AND 1=0
    }
}