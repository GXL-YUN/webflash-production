package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
/**
 * grooming_services
 * 洗护服务项目表
 */
@Data
@Entity(name = "pet_demand_grooming_services")
@Table(appliesTo = "pet_demand_grooming_services", comment = "洗护服务项目表")
@EntityListeners(AuditingEntityListener.class)
public class GroomingServices extends BaseEntity {

/**
 * id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 *   store_id UUID NOT NULL REFERENCES grooming_stores(id) ON DELETE CASCADE,
 *   name VARCHAR(100) NOT NULL,
 *   category VARCHAR(50) NOT NULL CHECK (category IN ('wash', 'grooming', 'spa')),
 *   description TEXT,
 *   price_config JSONB NOT NULL,
 *   duration INTEGER NOT NULL,
 *   available_slots JSONB,
 *   photos JSONB,
 *   is_available BOOLEAN DEFAULT true NOT NULL,
 *   sort_order INTEGER DEFAULT 0,
 *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
 *   updated_at TIMESTAMPTZ
 */



}