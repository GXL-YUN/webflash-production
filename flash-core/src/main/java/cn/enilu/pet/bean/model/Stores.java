package cn.enilu.pet.bean.model;
import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * 门店表
 */
@Data
@Entity(name = "pet_demand_stores")
@Table(appliesTo = "pet_demand_stores", comment = "门店表")
@EntityListeners(AuditingEntityListener.class)
public class Stores extends BaseEntity {


    /**
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   name VARCHAR(100) NOT NULL,
     *   logo_url TEXT,
     *   photos JSONB,
     *   address VARCHAR(200) NOT NULL,
     *   latitude NUMERIC(10, 7),
     *   longitude NUMERIC(10, 7),
     *   phone VARCHAR(20),
     *   rating NUMERIC(3, 2) DEFAULT 5.00 NOT NULL CHECK (rating >= 0 AND rating <= 5),
     *   reviews_count INTEGER DEFAULT 0 NOT NULL,
     *   distance NUMERIC(10, 2),
     *   opening_hours VARCHAR(50),
     *   is_open BOOLEAN DEFAULT true NOT NULL,
     *   description TEXT,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     *   updated_at TIMESTAMPTZ
     */
}
