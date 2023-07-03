package cn.enilu.kmss.bean.entity;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity(name = "t_record_file")
@Table(appliesTo = "t_record_file", comment = "档案")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RecordBean extends BaseEntity {

        @Column(columnDefinition = "VARCHAR(200) COMMENT '档案名称'")
        @NotNull(message = "档案名称不能为空")
        private String name;
        @Column(columnDefinition = "VARCHAR(128) COMMENT '档案标题'")
        @NotBlank(message = "档案标题不能为空")
        private String title;
        @Column(columnDefinition = "VARCHAR(128)  COMMENT '档案编号'")
        private String docNumber;
        @Column(columnDefinition = "VARCHAR(200)  COMMENT  '所属老师'")
        private String teacher;
        @Column(columnDefinition = "TEXT COMMENT '存放内容'")
        private String content;

        @Column(columnDefinition = "VARCHAR(64) COMMENT '作者'")
        private String author;

        @Column(columnDefinition = "VARCHAR(64) COMMENT '文章题图ID'")
        private String img;
}
