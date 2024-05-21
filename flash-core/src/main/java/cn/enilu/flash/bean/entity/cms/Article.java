package cn.enilu.flash.bean.entity.cms;

import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.system.FileInfo;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity(name = "t_cms_article")
@Table(appliesTo = "t_cms_article", comment = "文章")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Article extends BaseEntity {
    @Column(columnDefinition = "BIGINT COMMENT '栏目id'")
    @NotNull(message = "栏目不能为空")
    private Long idChannel;
    @Column(columnDefinition = "VARCHAR(128) COMMENT '标题'")
    @NotBlank(message = "标题不能为空")
    private String title;
    @Column(columnDefinition = "TEXT COMMENT '内容'")
    @NotBlank(message = "内容不能为空")
    private String content;
    @Column(columnDefinition = "VARCHAR(64) COMMENT '作者'")
    private String author;
    @Column(columnDefinition = "VARCHAR(64) COMMENT '文章题图ID'")
    private String img;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)//这里填写的是 Customer在LinkMan中的private属性,加上级联
    @JoinColumn(name = "file_id")
    private List<FileInfo> fileList = new ArrayList<>();
}
