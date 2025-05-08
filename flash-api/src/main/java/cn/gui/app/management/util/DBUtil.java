package cn.gui.app.management.util;

import java.sql.*;

public class DBUtil {
    //private static final String URL = "jdbc:mysql://122.51.246.240:3306/task_monitor?useSSL=false&serverTimezone=UTC";

    private static final String URL = "jdbc:mysql://122.51.246.240:3306/task_monitor?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    private static final String USER = "root";
    private static final String PASSWORD = "123qweASD.";

    static {
        try {
           // Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}