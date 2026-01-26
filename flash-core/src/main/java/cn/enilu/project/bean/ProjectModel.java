package cn.enilu.project.bean;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity(name = "t_project_list")
@Table(appliesTo = "t_project_list", comment = "项目管理")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ProjectModel extends BaseEntity  {

        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(columnDefinition = "CHAR(36)")
        private String fdCode;  // 存储为字符串格式

        @Column(columnDefinition = "VARCHAR(200) COMMENT '项目名称'")
        @NotNull(message = "项目名称不能为空")
        private String fdName;

        @Column(columnDefinition = "VARCHAR(200) COMMENT '项目描述'")
        private String fdMassage;

        @Column(columnDefinition = "VARCHAR(200) COMMENT '需求备注'")
        private String fdBz;

        @Column(columnDefinition = "VARCHAR(128) COMMENT '项目状态'")
        @NotNull(message = "项目状态不能为空")
        private String fdType;

        @Column(columnDefinition = "VARCHAR(200) COMMENT '项目对接人'")
        private String fdUserName;


        @Column(columnDefinition = "DATETIME COMMENT '项目开始时间'")
        private Date fdActoinTime;

        @Column(columnDefinition = "DATETIME COMMENT '项目结束时间'")
        private Date fdEndTime;


}
