package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.project.bean.model.ProjectModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 报价单数据
 */
@Data
@Entity(name = "pet_demand_quotes")
@Table(appliesTo = "pet_demand_quotes", comment = "报价单数据")
@EntityListeners(AuditingEntityListener.class)
public class Quotes extends BaseEntity {

    /**
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   demand_id UUID NOT NULL REFERENCES demands(id) ON DELETE CASCADE,
     *   merchant_id UUID NOT NULL REFERENCES merchants(id),
     *   merchant_name VARCHAR(100) NOT NULL,
     *   merchant_avatar TEXT,
     *   price NUMERIC(10, 2) NOT NULL,
     *   description TEXT,
     *   photos JSONB,
     *   videos JSONB,
     *   vaccine_records JSONB,
     *   deworming_records JSONB,
     *   birth_certificate TEXT,
     *   merchant_rating NUMERIC(3, 2) DEFAULT 5.00 CHECK (merchant_rating >= 0 AND merchant_rating <= 5),
     *   distance NUMERIC(10, 2),
     *   status VARCHAR(20) DEFAULT 'active' NOT NULL CHECK (status IN ('active', 'accepted', 'rejected', 'cancelled')),
     *   pet_name VARCHAR(50),
     *   pet_gender VARCHAR(10) CHECK (pet_gender IN ('male', 'female')),
     *   pet_age_months INTEGER,
     *   pet_color VARCHAR(50),
     *   vaccine_status VARCHAR(20),
     *   deworming_status VARCHAR(20),
     *   health_guarantee_days INTEGER,
     *   contact_name VARCHAR(50),
     *   contact_phone VARCHAR(20),
     *   view_count INTEGER DEFAULT 0,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     *   updated_at TIMESTAMPTZ
     */


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore  // 防止序列化时无限递归
    @JoinColumn(name = "fd_demand_id")  // 外键列
    private Demand fdDemand;  // 必须使用projectModel，而不是parent

    /** 商家名称*/
    @Column(name = "fd_merchant_name", columnDefinition = "VARCHAR(20) COMMENT '商家名称'")
    @NotNull(message = "商家名称")
    @JsonProperty("merchant_name")
    private String fdMerchantName;

   /**  报价*/
    @Column(name = "fd_price", columnDefinition = "DECIMAL(10,2) COMMENT '报价金额'")
    @JsonProperty("price")
    private BigDecimal fdPrice;


    /**
     * 报价中  已采纳    已拒绝  撤回报价   已取消
     */
    @Column(name = "fd_status", columnDefinition = "VARCHAR(20) DEFAULT '1' COMMENT '报价状态'")
    @JsonProperty("status")
    private String fdStatus;


   /** 评分*/
    @Column(name = "fd_merchant_rating", columnDefinition = "DECIMAL(10,2) COMMENT '评分'")
    @JsonProperty("merchant_rating")
    private BigDecimal fdMerchantRating;


/**     创建人ID*/
    @Column(name = "fd_user_id", columnDefinition = "VARCHAR(200) COMMENT '创建人ID'")
    @NotNull(message = "创建人IDID不能为空")
    @JsonProperty("user_id")
    private String fdUserId;

    /**详细描述*/
    @Column(name = "fd_description", columnDefinition = "TEXT COMMENT '详细描述'")
    @JsonProperty("description")
    private String fdDescription;
}
