package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
/**
 *
 * 预约表 appointments
 */
@Data
@Entity(name = "pet_demand_appointments")
@Table(appliesTo = "pet_demand_appointments", comment = "预约表")
@EntityListeners(AuditingEntityListener.class)
public class Appointments extends BaseEntity {

    /**
     *
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     store_id UUID NOT NULL REFERENCES stores(id),
     service_id UUID NOT NULL REFERENCES services(id),
     pet_id UUID REFERENCES pets(id),
     appointment_time TIMESTAMPTZ NOT NULL,
     status VARCHAR(20) DEFAULT 'pending' NOT NULL CHECK (status IN ('pending', 'confirmed', 'completed', 'cancelled')),
     note TEXT,
     price NUMERIC(10, 2) NOT NULL,
     verification_code VARCHAR(10),
     created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     updated_at TIMESTAMPTZ
     */
}
