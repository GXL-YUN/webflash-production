package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * 服务项目表  services
 */
@Data
@Entity(name = "pet_demand_services")
@Table(appliesTo = "pet_demand_services", comment = "服务项目表")
@EntityListeners(AuditingEntityListener.class)
public class PetServices extends BaseEntity {

    /**
     *  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   store_id UUID NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
     *   name VARCHAR(100) NOT NULL,
     *   description TEXT,
     *   price NUMERIC(10, 2) NOT NULL,
     *   duration INTEGER,
     *   photos JSONB,
     *   is_available BOOLEAN DEFAULT true NOT NULL,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     *   updated_at TIMESTAMPTZ
     */
}
