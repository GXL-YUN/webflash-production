package cn.enilu.project.bean.model;

import cn.enilu.flash.bean.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务功能点数据
 */
@Entity(name = "sys_project_tack")
@Table(appliesTo = "sys_project_tack", comment = "项目管理")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ProjectTack extends BaseEntity {
    // 这个属性的名称必须与ProjectModel中mappedBy的值一致
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore  // 防止序列化时无限递归
    @JoinColumn(name = "project_model_id")  // 外键列
    private ProjectModel projectModel;  // 必须使用projectModel，而不是parent

    @Column(columnDefinition = "VARCHAR(200) COMMENT '任务点名称'")
    @NotNull(message = "确认项名称")
    private String fdName;

    @Column(columnDefinition = "VARCHAR(2000) COMMENT '任务点描述'")
    private String fdMassage;

    @Column(columnDefinition = "DATETIME COMMENT '任务开始时间'")
    private Date fdActoinTime;

    @Column(columnDefinition = "DATETIME COMMENT '任务结束时间'")
    private Date fdEndTime;

    @Column(columnDefinition = "VARCHAR(2000) COMMENT '任务点状态'")
    private String fdStatre;

    @Column(columnDefinition = "DATETIME COMMENT '确认关闭时间'")
    private Date fdCloseTime;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '是否风险项'")
    private boolean fdType;


    @OneToMany(
            mappedBy = "fdProjectTack",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<ProjectItem> fdProjectTack = new ArrayList<>();


}
