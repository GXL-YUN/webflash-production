package cn.enilu.project.bean.model;

import cn.enilu.flash.bean.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
// ❌ 删除这一行
// import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * 项目确认项
 */
@Data
@Entity(name = "sys_project_item")
// ✅ 使用JPA的 @Table 注解
@javax.persistence.Table(name = "sys_project_item")  // 正确的注解
@EntityListeners(AuditingEntityListener.class)
public class ProjectItem extends BaseEntity  {

    // 这个属性的名称必须与ProjectModel中mappedBy的值一致
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore  // 防止序列化时无限递归
    @JoinColumn(name = "project_model_id", referencedColumnName = "fd_id")  // 明确指定引用哪个字段)
    private ProjectModel projectModel;  // 必须使用projectModel，而不是parent

    @Column(columnDefinition = "VARCHAR(200) COMMENT '项目名称'")
    @NotNull(message = "确认项名称")
    private String fdName;

    @Column(columnDefinition = "VARCHAR(2000) COMMENT '描述'")
    private String fdMassage;

    @Column(columnDefinition = "DATETIME COMMENT '项目开始时间'")
    private Date fdActoinTime;

    @Column(columnDefinition = "DATETIME COMMENT '项目结束时间'")
    private Date fdEndTime;

    @Column(columnDefinition = "DATETIME COMMENT '确认关闭时间'")
    private Date fdCloseTime;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '是否风险项'")
    private boolean fdType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore  // 防止序列化时无限递归
    @JoinColumn(name = "fd_project_Tack_id")  // 外键列
    private ProjectModel fdProjectTack;  // 必须使用projectModel，而不是parent
}