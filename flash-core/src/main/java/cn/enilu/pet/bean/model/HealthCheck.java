package cn.enilu.pet.bean.model;

import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.project.bean.model.ProjectModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


/**
 * health_check
 * 健康检查表
 */
@Data
@Entity(name = "pet_demand_health_check")
@Table(appliesTo = "pet_demand_health_check", comment = "健康检查表")
@EntityListeners(AuditingEntityListener.class)
public class HealthCheck extends BaseEntity {

    /**
     *   id SERIAL PRIMARY KEY,
     *   updated_at TIMESTAMPTZ DEFAULT NOW()
     */


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore  // 防止序列化时无限递归
    @JoinColumn(name = "fd_adopt_id")  // 外键列
    private Adopt fdAdopt;  // 必须使用projectModel，而不是parent
}
