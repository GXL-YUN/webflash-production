package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * merchant_bank_accounts
 *
 * 商家银行账户表
 */
@Data
@Entity(name = "pet_demand_merchant_bank_accounts")
@Table(appliesTo = "pet_demand_merchant_bank_accounts", comment = "商家银行账户表")
@EntityListeners(AuditingEntityListener.class)
public class MerchantBankAccounts extends BaseEntity {
    /**
     *   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     *   merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE CASCADE,
     *   bank_name VARCHAR(50) NOT NULL,
     *   account_name VARCHAR(100) NOT NULL,
     *   account_no VARCHAR(50) NOT NULL,
     *   bank_branch VARCHAR(100),
     *   is_default BOOLEAN DEFAULT false NOT NULL,
     *   status VARCHAR(20) DEFAULT 'active' NOT NULL CHECK (status IN ('active', 'disabled')),
     *   created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
     *   updated_at TIMESTAMPTZ
     */
}
