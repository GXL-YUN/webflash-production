package cn.enilu.flash.api.config.date;

import cn.hutool.db.ds.DSFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConnectionMkOracleWrapper {

    @Value("${DB_mk_oralce}")
    private String configStr;

    /**
     * 获取Mk oracle连接
     * @return 数据库连接
     * @throws SQLException 数据库异常
     */
    public Connection getConnection() throws SQLException {
        try {
            DataSource dataSource = DSFactory.get(configStr);
            if (dataSource == null) {
                throw new SQLException("无法获取数据源");
            }
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new SQLException("获取数据库连接失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取连接并验证连接状态
     * @return 有效的数据库连接
     * @throws SQLException 数据库异常
     */
    public Connection getValidConnection() throws SQLException {
        Connection conn = getConnection();
        if (conn == null || conn.isClosed()) {
            throw new SQLException("无法获取有效的数据库连接");
        }
        return conn;
    }
}
