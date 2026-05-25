package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * 宠物相册 pet_photos
 */
@Data
@Entity(name = "pet_demand_pet_photos")
@Table(appliesTo = "pet_demand_pet_photos", comment = "宠物相册")
@EntityListeners(AuditingEntityListener.class)
public class PetPhotos extends BaseEntity {
    /**
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   pet_id UUID NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
     *   photo_key VARCHAR(500) NOT NULL,
     *   photo_url TEXT,
     *   description TEXT,
     *   sort_order INTEGER DEFAULT 0,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
     */
}
