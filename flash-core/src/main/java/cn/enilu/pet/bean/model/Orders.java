package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 */
@Data
@Entity(name = "pet_demand_orders")
@Table(appliesTo = "pet_demand_orders", comment = "订单表")
@EntityListeners(AuditingEntityListener.class)
public class Orders extends BaseEntity {

    /**
     *
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     user_id UUID NOT NULL REFERENCES users(id),
     quote_id UUID, -- 外键在后面单独添加
     order_type VARCHAR(20) NOT NULL CHECK (order_type IN ('pet', 'grooming')),
     status VARCHAR(20) DEFAULT 'pending' NOT NULL CHECK (status IN ('pending', 'paid', 'delivering', 'in_service', 'completed', 'cancelled')),
     total_amount NUMERIC(10, 2) NOT NULL,
     deposit_amount NUMERIC(10, 2),
     paid_amount NUMERIC(10, 2),
     address_id UUID REFERENCES addresses(id),
     health_report_url TEXT,
     logistics_info JSONB,
     appointment_id UUID REFERENCES appointments(id),
     appointment_time TIMESTAMPTZ,
     verification_code VARCHAR(20),
     appointment JSONB,
     created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     updated_at TIMESTAMPTZ
     */


    // 订单编号
    @Column(name = "fd_order_no", columnDefinition = "VARCHAR(100) COMMENT '订单编号'", updatable = false)
    @NotNull(message = "订单编号")
    @JsonProperty("order_no")
    private String fdOrderNo;

    // 用户id
    @Column(name = "fd_user_id", columnDefinition = "VARCHAR(100) COMMENT '用户id'", updatable = false)
    @NotNull(message = "用户id")
    @JsonProperty("user_id")
    private String fdUserId;


    // 业务单据id
    @Column(name = "product_id", columnDefinition = "VARCHAR(100) COMMENT '产品id'", updatable = false)
    @NotNull(message = "产品id")
    @JsonProperty("product_id")
    private String fdProductId;


    // 支付金额
    @Column(name = "fd_amount", columnDefinition = "DECIMAL(10,2) COMMENT '支付金额'", updatable = false)
    @JsonProperty("amount")
    private BigDecimal fdAmount;


    // 单据状态   0 待支付 1 已支付 2 已取消
    @Column(name = "fd_status", columnDefinition = "VARCHAR(100) DEFAULT '0' COMMENT '单据状态'")
    @NotNull(message = "单据状态")
    @JsonProperty("status")
    private String fdStatus;


    // 地址
    @Column(name = "fd_address_id", columnDefinition = "VARCHAR(100) COMMENT '地址'")
    @NotNull(message = "地址")
    @JsonProperty("address_id")
    private String fdAddressId;

    // 在保存前自动设置
    @PrePersist
    protected void onCreate() {

    }

}
