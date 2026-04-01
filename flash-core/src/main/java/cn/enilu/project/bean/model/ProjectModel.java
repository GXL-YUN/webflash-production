package cn.enilu.project.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.att.AttMainMassage;
import cn.enilu.flash.bean.entity.att.AttachmentInfo;
import cn.enilu.flash.bean.entity.system.FileInfo;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 需求项目列表数据
 */
@Entity(name = "sys_project_list")
@Table(appliesTo = "sys_project_list", comment = "项目管理")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ProjectModel extends BaseEntity  {


        @Column(name = "fd_code", columnDefinition = "VARCHAR(50) COMMENT '项目编码'")
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


        @Column(columnDefinition = "TEXT")
        private String richTextContent;
//1.2 使用 LONGTEXT类型（支持更大内容）
        @Column(columnDefinition = "LONGTEXT")
        private String richTextContent_long;
        /**
         *项目附件
         */
        //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)//这里填写的是 Customer在LinkMan中的private属性,加上级联
       // @JoinColumn(name = "fd_file_id")

//
//        @OneToMany(
//                mappedBy = "projectModel",
//                cascade = CascadeType.ALL,
//                fetch = FetchType.LAZY,
//                orphanRemoval = true
//        )
//        private List<FileInfo> fileList = new ArrayList<>();

        /**
         * 待确认项
         */
      //  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)//这里填写的是 Customer在LinkMan中的private属性,加上级联
      //  @JoinColumn(name = "fd_confirmation_items_id")

        @OneToMany(
                mappedBy = "projectModel",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                orphanRemoval = true
        )
        private List<ProjectItem> fileLists = new ArrayList<>();
        /**
         * 项目风险点
         */
        @OneToMany(
                mappedBy = "projectModel",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                orphanRemoval = true
        )
        private List<ProjectRisk> fileListss = new ArrayList<>();

        /**
         * 功能点任务点
         */
        @OneToMany(
                mappedBy = "projectModel",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                orphanRemoval = true
        )
        private List<ProjectTack> fdProjectTack = new ArrayList<>();




}
