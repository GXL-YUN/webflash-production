package cn.enilu.vode.bean.entity;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity(name = "t_vode_file")
@Table(appliesTo = "t_vode_file", comment = "视频")
@Data
@EntityListeners(AuditingEntityListener.class)
public class VodeBean extends BaseEntity {

    @Column(columnDefinition = "VARCHAR(400) COMMENT '视频描述'")
    @NotNull(message = "视频描述")
    private String massage;

    @Column(columnDefinition = "VARCHAR(400) COMMENT '图片'")
    @NotNull(message = "图片")
    private String imgId;

    @Column(columnDefinition = "VARCHAR(400) COMMENT '视频'")
    @NotNull(message = "视频")
    private String videId;



}
