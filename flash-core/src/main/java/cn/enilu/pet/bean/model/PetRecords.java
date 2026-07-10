package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * 成长记录 pet_records
 */
@Data
@Entity(name = "pet_demand_pet_records")
@Table(appliesTo = "pet_demand_pet_records", comment = "成长记录")
@EntityListeners(AuditingEntityListener.class)
public class PetRecords extends BaseEntity {

    /**
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   pet_id UUID NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
     *   record_type VARCHAR(20) NOT NULL CHECK (record_type IN ('weight', 'bath', 'medical', 'vaccine', 'deworming', 'other')),
     *   record_date TIMESTAMPTZ NOT NULL,
     *   value NUMERIC(10, 2),
     *   unit VARCHAR(10),
     *   description TEXT,
     *   note TEXT,
     *   photos JSONB,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
     */
}
