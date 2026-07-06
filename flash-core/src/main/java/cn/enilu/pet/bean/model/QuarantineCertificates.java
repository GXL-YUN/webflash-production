package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
/**
 * quarantine_certificates   检疫证明表
 */
@Data
@Entity(name = "pet_demand_quarantine_certificates")
@Table(appliesTo = "pet_demand_quarantine_certificates", comment = "检疫证明表")
@EntityListeners(AuditingEntityListener.class)
public class QuarantineCertificates extends BaseEntity {

    /**
     *  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
     *   merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE CASCADE,
     *   certificate_no VARCHAR(50) NOT NULL,
     *   certificate_url TEXT,
     *   issue_date TIMESTAMPTZ,
     *   valid_until TIMESTAMPTZ,
     *   issued_by VARCHAR(100),
     *   status VARCHAR(20) DEFAULT 'valid' NOT NULL CHECK (status IN ('valid', 'expired', 'cancelled')),
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
     */
}
