package cn.enilu.project.bean.model;

import cn.enilu.flash.bean.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Entity(name = "sys_project_rick")
@Table(appliesTo = "sys_project_rick", comment = "风险点")
@EntityListeners(AuditingEntityListener.class)
public class ProjectRisk extends BaseEntity {
    // 这个属性的名称必须与ProjectModel中mappedBy的值一致
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore  // 防止序列化时无限递归
    @JoinColumn(name = "project_model_id")  // 外键列
    private ProjectModel projectModel;  // 必须使用projectModel，而不是parent


    @Column(columnDefinition = "VARCHAR(200) COMMENT '风险名称'")
    @NotNull(message = "风险名称")
    private String fdName;

    @Column(columnDefinition = "VARCHAR(2000) COMMENT '描述'")
    private String fdMassage;

    @Column(columnDefinition = "DATETIME COMMENT '项目开始时间'")
    private Date fdActoinTime;

    @Column(columnDefinition = "DATETIME COMMENT '预计结束时间'")
    private Date fdEndTime;


    @Column(columnDefinition = "DATETIME COMMENT '确认关闭时间'")
    private Date fdCloseTime;



}
