package cn.enilu.flash.api.utils.sql;

import cn.enilu.flash.api.config.date.ConnectionMkOracleWrapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 通用插入数据
 */
@Component
public class NamedParameterSqlExecutor {

    @Resource
    private ConnectionMkOracleWrapper connectionMkOracleWrapper;

    private static final Pattern NAMED_PARAM_PATTERN = Pattern.compile(":(\\w+)");

    /**
     * 执行命名参数SQL查询
     */
    public List<Map<String, Object>> executeQuery(String sql, Map<String, Object> paramMap) throws SQLException {
        ParametrizedSql parametrizedSql = parseNamedParameters(sql, paramMap);

        try (Connection ekpConn = connectionMkOracleWrapper.getConnection();
             PreparedStatement pstmt = ekpConn.prepareStatement(parametrizedSql.getActualSql())) {

            setNamedParameters(pstmt, parametrizedSql);

            try (ResultSet rs = pstmt.executeQuery()) {
                return convertResultSetToList(rs);
            }
        }
    }

    /**
     * 执行命名参数SQL更新
     */
    public int executeUpdate(String sql, Map<String, Object> paramMap) throws SQLException {
        ParametrizedSql parametrizedSql = parseNamedParameters(sql, paramMap);

        try (Connection ekpConn = connectionMkOracleWrapper.getConnection();
             PreparedStatement pstmt = ekpConn.prepareStatement(parametrizedSql.getActualSql())) {

            setNamedParameters(pstmt, parametrizedSql);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 执行插入并返回主键
     */
    public Long executeInsertAndReturnKey(String sql, Map<String, Object> paramMap, String keyColumn) throws SQLException {
        ParametrizedSql parametrizedSql = parseNamedParameters(sql, paramMap);

        try (Connection ekpConn = connectionMkOracleWrapper.getConnection();
             PreparedStatement pstmt = ekpConn.prepareStatement(sql, new String[]{keyColumn})) {

            setNamedParameters(pstmt, parametrizedSql);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
            return null;
        }
    }

    /**
     * 解析命名参数SQL
     */
    private ParametrizedSql parseNamedParameters(String sql, Map<String, Object> paramMap) {
        List<String> paramNames = new ArrayList<>();
        StringBuffer actualSql = new StringBuffer();
        Matcher matcher = NAMED_PARAM_PATTERN.matcher(sql);

        while (matcher.find()) {
            String paramName = matcher.group(1);
            paramNames.add(paramName);
            matcher.appendReplacement(actualSql, "?");
        }
        matcher.appendTail(actualSql);

        // 验证参数
        List<Object> paramValues = new ArrayList<>();
        for (String paramName : paramNames) {
            if (!paramMap.containsKey(paramName)) {
                throw new IllegalArgumentException("缺少参数: " + paramName);
            }
            paramValues.add(paramMap.get(paramName));
        }

        return new ParametrizedSql(actualSql.toString(), paramValues);
    }

    /**
     * 设置命名参数
     */
    private void setNamedParameters(PreparedStatement pstmt, ParametrizedSql parametrizedSql) throws SQLException {
        List<Object> paramValues = parametrizedSql.getParamValues();
        for (int i = 0; i < paramValues.size(); i++) {
            pstmt.setObject(i + 1, paramValues.get(i));
        }
    }

    /**
     * ResultSet转换为List
     */
    private List<Map<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            resultList.add(row);
        }
        return resultList;
    }

    /**
     * 参数化SQL封装类
     */
    private static class ParametrizedSql {
        private final String actualSql;
        private final List<Object> paramValues;

        public ParametrizedSql(String actualSql, List<Object> paramValues) {
            this.actualSql = actualSql;
            this.paramValues = paramValues;
        }

        public String getActualSql() {
            return actualSql;
        }

        public List<Object> getParamValues() {
            return paramValues;
        }
    }
}