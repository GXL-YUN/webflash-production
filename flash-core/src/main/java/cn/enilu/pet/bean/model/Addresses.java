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
 * 收货地址表 addresses
 */
@Data
@Entity(name = "pet_demand_addresses")
@Table(appliesTo = "pet_demand_addresses", comment = "收货地址表")
@EntityListeners(AuditingEntityListener.class)
public class Addresses extends BaseEntity {

    /**
     *  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     *   receiver_name VARCHAR(50) NOT NULL,
     *   receiver_phone VARCHAR(20) NOT NULL,
     *   province VARCHAR(50) NOT NULL,
     *   city VARCHAR(50) NOT NULL,
     *   district VARCHAR(50) NOT NULL,
     *   detail_address VARCHAR(200) NOT NULL,
     *   is_default BOOLEAN DEFAULT false NOT NULL,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     *   updated_at TIMESTAMPTZ
     */
    // 需求人ID
    @Column(name = "fd_user_id", columnDefinition = "VARCHAR(200) COMMENT '需求人ID'")
    @NotNull(message = "需求人ID不能为空")
    @JsonProperty("user_id")
    private String fdUserId;


    @Column(name = "fd_receiver_name", columnDefinition = "VARCHAR(200) COMMENT '联系人'")
    @JsonProperty("receiver_name")
    private String fdReceiverName;


    @Column(name = "fd_receiver_phone", columnDefinition = "VARCHAR(200) COMMENT '联系人电话'")
    @JsonProperty("receiver_phone")
    private String fdReceiverPhone;



    @Column(name = "fd_province", columnDefinition = "VARCHAR(200) COMMENT '省'")
    @JsonProperty("province")
    private String fdRrovince;


    @Column(name = "fd_city", columnDefinition = "VARCHAR(200) COMMENT '市区'")
    @JsonProperty("city")
    private String fdCity;


    @Column(name = "fd_district", columnDefinition = "VARCHAR(200) COMMENT '区'")
    @JsonProperty("district")
    private String fdDistrict;


    @Column(name = "fd_detail_address", columnDefinition = "VARCHAR(200) COMMENT '默认地址'")
    @JsonProperty("detail_address")
    private String fdDetailAddress;


    // 是否打疫苗
    @Column(name = "fd_is_default", columnDefinition = "BOOLEAN DEFAULT true COMMENT '是否默认地址'")
    @JsonProperty("is_default")
    private Boolean fdIsDefault;


}
