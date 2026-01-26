package cn.enilu.flash.api.utils.sql;


import com.rabbitmq.client.Connection;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


public class UserService {


    private NamedParameterSqlExecutor sqlExecutor;

    /**
     * 插入用户 - 使用命名参数
     */
    public Long insertUser(String username, String email) throws SQLException {
        String sql = "INSERT INTO t_user (fd_col_hpla2d, fd_col_email) VALUES (:fdColHpla2d, :fdColEmail)";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fdColHpla2d", username);
        paramMap.put("fdColEmail", email);

        return sqlExecutor.executeInsertAndReturnKey(sql, paramMap, "fd_col_id");
    }

    /**
     * 批量插入用户
     */
//    public int[] batchInsertUsers(List<Map<String, Object>> userList) throws SQLException {
//        String sql = "INSERT INTO t_user (fd_col_hpla2d, fd_col_email, fd_col_phone) VALUES (:fdColHpla2d, :fdColEmail, :fdColPhone)";
//
//        try (                                                                                                                                                                                                                        Connection ekpConn = connectionMkOracleWrapper.getConnection();
//             PreparedStatement pstmt = ekpConn.prepareStatement(sql)) {
//
//            for (Map<String, Object> user : userList) {
//                pstmt.setString(1, (String) user.get("fdColHpla2d"));
//                pstmt.setString(2, (String) user.get("fdColEmail"));
//                pstmt.setString(3, (String) user.get("fdColPhone"));
//                pstmt.addBatch();
//            }
//
//            return pstmt.executeBatch();
//        }
//    }

    /**
     * 查询用户
     */
    public List<Map<String, Object>> getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM t_user WHERE fd_col_hpla2d = :fdColHpla2d";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fdColHpla2d", username);

        return sqlExecutor.executeQuery(sql, paramMap);
    }

    /**
     * 更新用户信息
     */
    public int updateUserEmail(Long userId, String newEmail) throws SQLException {
        String sql = "UPDATE t_user SET fd_col_email = :fdColEmail WHERE fd_col_id = :fdColId";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fdColEmail", newEmail);
        paramMap.put("fdColId", userId);

        return sqlExecutor.executeUpdate(sql, paramMap);
    }

    /**
     * 复杂查询示例
     */
    public List<Map<String, Object>> searchUsers(String keyword, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT * FROM t_user " +
                "WHERE (fd_col_hpla2d LIKE :keyword OR fd_col_email LIKE :keyword) " +
                "AND fd_create_time BETWEEN :startDate AND :endDate " +
                "ORDER BY fd_create_time DESC";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("keyword", "%" + keyword + "%");
        paramMap.put("startDate", new java.sql.Date(startDate.getTime()));
        paramMap.put("endDate", new java.sql.Date(endDate.getTime()));

        return sqlExecutor.executeQuery(sql, paramMap);
    }
}