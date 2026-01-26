package cn.enilu.flash.api.utils.sql;

import cn.enilu.flash.api.utils.sql.bean.User;
import cn.enilu.flash.api.utils.sql.util.EnhancedSqlGenerator;
import cn.enilu.flash.api.utils.sql.util.SqlGenerator;

import java.util.Arrays;
import java.util.List; /**
 * 使用示例
 */
public class SqlGeneratorExample {


    public static void main(String[] args) {
        // 创建用户对象
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        // phone和status为null

        // 生成插入SQL（只包含有值的字段）
        String insertSql = SqlGenerator.generateInsertSql(user);
        System.out.println("插入SQL: " + insertSql);
        // 输出: INSERT INTO t_user (fd_col_hpla2d, fd_col_email) VALUES (:fdColHpla2d, :fdColEmail)

        // 生成更新SQL（只更新有值的字段）
        String updateSql = SqlGenerator.generateUpdateSql(user);
        System.out.println("更新SQL: " + updateSql);
        // 输出: UPDATE t_user SET fd_col_hpla2d = :fdColHpla2d, fd_col_email = :fdColEmail WHERE fd_col_id = :fdColId

        // 生成删除SQL
        String deleteSql = SqlGenerator.generateDeleteSql(user);
        System.out.println("删除SQL: " + deleteSql);
        // 输出: DELETE FROM t_user WHERE fd_col_id = :fdColId

        // 生成查询SQL（只包含有值的字段作为条件）
        String selectSql = SqlGenerator.generateSelectSql(user, false);
        System.out.println("查询SQL: " + selectSql);
        // 输出: SELECT * FROM t_user WHERE fd_col_hpla2d = :fdColHpla2d AND fd_col_email = :fdColEmail

        // 使用增强版生成器（带自动填充）
        User newUser = new User();
        newUser.setUsername("jane_doe");

        String insertWithAutoFill = EnhancedSqlGenerator.generateInsertSqlWithAutoFill(newUser);
        System.out.println("带自动填充的插入SQL: " + insertWithAutoFill);

        // 批量操作
        List<User> users = Arrays.asList(user, newUser);
        String batchInsertSql = EnhancedSqlGenerator.generateBatchInsertSql(users);
        System.out.println("批量插入SQL: " + batchInsertSql);
    }
}
