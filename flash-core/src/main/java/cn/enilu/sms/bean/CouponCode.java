package cn.enilu.sms.bean;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.sql.Date;

/**
 * 核销码
 */
@Data
@Entity(name = "sys_coupon_code")
@Table(appliesTo = "sys_coupon_code", comment = "项目管理")
@EntityListeners(AuditingEntityListener.class)
public class CouponCode extends BaseEntity {

    @Column(name = "verifyCode", columnDefinition = "VARCHAR(50) COMMENT '校验码'")
    private String verifyCode;

    @Column(name = "bizType", columnDefinition = "VARCHAR(50) COMMENT ''")
    private String bizType;

    @Column(name = "userId", columnDefinition = "VARCHAR(50) COMMENT '用户id'")
    private String userId;

    @Column(name = "fd_code", columnDefinition = "VARCHAR(50) COMMENT '核销状态'")
    private Integer status;

    @Column(name = "expireTime", columnDefinition = "VARCHAR(50) COMMENT '有效时间'")
    private Date expireTime;

    @Column(name = "usedTime", columnDefinition = "VARCHAR(50) COMMENT '使用时间'")
    private Date usedTime;
}