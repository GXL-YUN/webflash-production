package cn.enilu.pet.bean.model;
import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * Verifications
 *
 * 核销记录
 */
@Data
@Entity(name = "pet_demand_verifications")
@Table(appliesTo = "pet_demand_verifications", comment = "核销记录")
@EntityListeners(AuditingEntityListener.class)
public class Verifications extends BaseEntity {


    /**
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   appointment_id UUID NOT NULL REFERENCES appointments(id) ON DELETE CASCADE,
     *   store_id UUID NOT NULL,
     *   verification_code VARCHAR(20) NOT NULL,
     *   verified_by UUID,
     *   verify_method VARCHAR(20) NOT NULL CHECK (verify_method IN ('scan', 'manual')),
     *   status VARCHAR(20) DEFAULT 'success' NOT NULL CHECK (status IN ('success', 'failed')),
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
     */



}
