package cn.enilu.flash.api.utils.sql.bean;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 示例实体类
 */
@Data
public class User extends BaseEntity {

    @Alias(value = "fd_col_id", isPrimaryKey = true)
    private Long id;

    @Alias(value = "fd_col_hpla2d", required = true)
    private String username;

    @Alias(value = "fd_col_email")
    private String email;

    @Alias(value = "fd_col_phone")
    private String phone;

    @Alias(value = "fd_col_status")
    private Integer status;

    @Alias(value = "fd_create_time", autoCreate = true)
    private LocalDateTime createTime;

    @Alias(value = "fd_update_time", autoUpdate = true)
    private LocalDateTime updateTime;

    @Override
    public String getTableName() {
        return "t_user"; // 自定义表名
    }
}

