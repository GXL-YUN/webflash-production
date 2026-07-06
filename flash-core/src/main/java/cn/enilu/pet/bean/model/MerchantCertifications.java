package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
/**
 * 商家认证记录 merchant_certifications
 */
@Data
@Entity(name = "pet_demand_merchant_certifications")
@Table(appliesTo = "pet_demand_merchant_certifications", comment = "商家认证记录")
@EntityListeners(AuditingEntityListener.class)
public class MerchantCertifications extends BaseEntity {
    /**
     *  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE CASCADE,
     *   certification_type VARCHAR(20) NOT NULL CHECK (certification_type IN ('initial', 'renewal')),
     *   business_license_url TEXT,
     *   id_card_front_url TEXT,
     *   id_card_back_url TEXT,
     *   environment_photos JSONB,
     *   live_pet_license_url TEXT,
     *   status VARCHAR(20) DEFAULT 'pending' NOT NULL CHECK (status IN ('pending', 'approved', 'rejected')),
     *   rejection_reason TEXT,
     *   reviewer_id UUID,
     *   reviewed_at TIMESTAMPTZ,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
     */
}
