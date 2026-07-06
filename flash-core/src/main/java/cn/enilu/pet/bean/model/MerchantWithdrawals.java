package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * merchant_withdrawals  商家体现记录
 */
@Data
@Entity(name = "pet_demand_merchant_withdrawals")
@Table(appliesTo = "pet_demand_merchant_withdrawals", comment = "商家体现记录")
@EntityListeners(AuditingEntityListener.class)
public class MerchantWithdrawals extends BaseEntity {
    /**
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE CASCADE,
     *   bank_account_id UUID REFERENCES merchant_bank_accounts(id),
     *   amount NUMERIC(10, 2) NOT NULL,
     *   fee NUMERIC(10, 2) DEFAULT 0,
     *   status VARCHAR(20) DEFAULT 'pending' NOT NULL CHECK (status IN ('pending', 'processing', 'completed', 'failed')),
     *   failed_reason TEXT,
     *   completed_at TIMESTAMPTZ,
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     *   updated_at TIMESTAMPTZ
     */
}
