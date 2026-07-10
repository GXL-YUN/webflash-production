package cn.enilu.pet.bean.model;



import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * groomer_certificates
 * 美容师资质表
 */
@Data
@Entity(name = "pet_demand_groomer_certificates")
@Table(appliesTo = "pet_demand_groomer_certificates", comment = "美容师资质表")
@EntityListeners(AuditingEntityListener.class)
public class GroomerCertificates extends BaseEntity {


    /**
     *
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE CASCADE,
     *   groomer_name VARCHAR(50) NOT NULL,
     *   certificate_type VARCHAR(50) NOT NULL,
     *   certificate_no VARCHAR(100),
     *   certificate_url TEXT,
     *   issue_date TIMESTAMPTZ,
     *   valid_until TIMESTAMPTZ,
     *   status VARCHAR(20) DEFAULT 'valid' NOT NULL CHECK (status IN ('valid', 'expired')),
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
     */
}
