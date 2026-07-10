package cn.enilu.pet.bean.model;



import cn.enilu.flash.bean.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * appointment_cancellations
 *
 * 预约取消记录表
 */
@Data
@Entity(name = "pet_demand_appointment_cancellations")
@Table(appliesTo = "pet_demand_appointment_cancellations", comment = "预约取消记录表")
@EntityListeners(AuditingEntityListener.class)
public class AppointmentCancellations extends BaseEntity {


    /**}
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   appointment_id UUID NOT NULL REFERENCES appointments(id) ON DELETE CASCADE,
     *   cancelled_by VARCHAR(20) NOT NULL CHECK (cancelled_by IN ('user', 'merchant')),
     *   reason TEXT,
     *   refund_amount NUMERIC(10, 2),
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
     */

    // 需求人ID
    @Column(name = "fd_user_id", columnDefinition = "VARCHAR(200) COMMENT '需求人ID'")
    @NotNull(message = "需求人ID不能为空")
    @JsonProperty("user_id")
    private String fdUserId;



    // 详细描述
    @Column(name = "fd_reason", columnDefinition = "TEXT COMMENT '原因'")
    @JsonProperty("reason")
    private String fdReason;


    @Column(name = "fd_refund_amount", columnDefinition = "DECIMAL(10,2) COMMENT '最低预算'")
    @JsonProperty("refund_amount")
    private BigDecimal fdRefundAmount;




}
