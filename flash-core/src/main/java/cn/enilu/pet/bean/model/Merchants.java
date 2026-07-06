package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * 商家表 merchants
 */
@Data
@Entity(name = "pet_demand_merchants")
@Table(appliesTo = "pet_demand_merchants", comment = "商家表")
@EntityListeners(AuditingEntityListener.class)
public class Merchants extends BaseEntity {
    /**
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   phone VARCHAR(20) NOT NULL UNIQUE,
     *   name VARCHAR(100),
     *   avatar_url TEXT,
     *   type VARCHAR(20) NOT NULL CHECK (type IN ('breeder', 'grooming', 'both')),
     *   status VARCHAR(20) DEFAULT 'pending' NOT NULL CHECK (status IN ('pending', 'certified', 'rejected', 'banned')),
     *   business_license_url TEXT,
     *   id_card_front_url TEXT,
     *   id_card_back_url TEXT,
     *   environment_photos JSONB,
     *   live_pet_license_url TEXT,
     *   certification_submitted_at TIMESTAMPTZ,
     *   certification_verified_at TIMESTAMPTZ,
     *   rejection_reason TEXT,
     *   province VARCHAR(50),
     *   city VARCHAR(50),
     *   district VARCHAR(50),
     *   address VARCHAR(200),
     *   description TEXT,
     *   rating NUMERIC(3, 2) DEFAULT 5.00 CHECK (rating >= 0 AND rating <= 5),
     *   orders_count INTEGER DEFAULT 0,
     *   quotes_count INTEGER DEFAULT 0,
     *   balance NUMERIC(10, 2) DEFAULT 0,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     *   updated_at TIMESTAMPTZ
     * );
     */
}
