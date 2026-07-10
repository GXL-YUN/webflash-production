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


/**
 * 支付记录
 */
@Data
@Entity(name = "pet_payment_record")
//@Table(appliesTo = "pet_payment_record", comment = "支付记录表")
@EntityListeners(AuditingEntityListener.class)
public class Payment_record extends BaseEntity {


    // 订单编号
    @Column(name = "fd_order_no", columnDefinition = "VARCHAR(100) COMMENT '订单编号'", updatable = false)
    @NotNull(message = "订单编号")
    @JsonProperty("order_no")
    private String fdOrderNo;


    // 单据状态   1 支付宝 2 微信
    @Column(name = "fd_pay_channel", columnDefinition = "VARCHAR(100)  COMMENT '支付方式'")
    @NotNull(message = "支付方式")
    @JsonProperty("pay_channel")
    private String fdPayChannel;

    // 单据状态   0 待支付 1 已支付 2 已取消
    @Column(name = "fd_pay_status", columnDefinition = "VARCHAR(100) DEFAULT '0' COMMENT '单据状态'")
    @NotNull(message = "单据状态")
    @JsonProperty("pay_status")
    private String fdPayStatus;

    @Column(name = "fd_third_pay_no", columnDefinition = "VARCHAR(128)  COMMENT '未知'")
    @NotNull(message = "未知")
    @JsonProperty("third_pay_no")
    private String fdTthirdPayNo;

}
