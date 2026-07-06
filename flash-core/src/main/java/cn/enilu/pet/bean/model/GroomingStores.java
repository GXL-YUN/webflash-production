package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;


/**
 * grooming_stores   洗护商家门店信息表
 */
@Data
@Entity(name = "pet_demand_grooming_stores")
@Table(appliesTo = "pet_demand_grooming_stores", comment = "洗护商家门店信息表")
@EntityListeners(AuditingEntityListener.class)
public class GroomingStores extends BaseEntity {
    /**
     *  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE CASCADE,
     *   name VARCHAR(100) NOT NULL,
     *   logo_url TEXT,
     *   phone VARCHAR(20) NOT NULL,
     *   province VARCHAR(50) NOT NULL,
     *   city VARCHAR(50) NOT NULL,
     *   district VARCHAR(50) NOT NULL,
     *   address VARCHAR(200) NOT NULL,
     *   latitude NUMERIC(10, 7),
     *   longitude NUMERIC(10, 7),
     *   opening_hours VARCHAR(100),
     *   service_range VARCHAR(50),
     *   description TEXT,
     *   photos JSONB,
     *   rating NUMERIC(3, 2) DEFAULT 5.00 CHECK (rating >= 0 AND rating <= 5),
     *   reviews_count INTEGER DEFAULT 0,
     *   is_open BOOLEAN DEFAULT true NOT NULL,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     *   updated_at TIMESTAMPTZ
     */

}
