package cn.enilu.flash.bean.entity.system;

import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.cms.Article;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "t_sys_file_info")
@Table(appliesTo = "t_sys_file_info", comment = "文件")
@EntityListeners(AuditingEntityListener.class)
public class FileInfo extends BaseEntity {



    @Column(columnDefinition = "VARCHAR(64) COMMENT '原始文件名称'")
    private String originalFileName;
    @Column(columnDefinition = "VARCHAR(64) COMMENT '文件存储在磁盘中的真正名称'")
    private String realFileName;

    @Transient
    private String ablatePath;


/*    @Column(columnDefinition = "VARCHAR(64) COMMENT '模版id'")
    private String fdModelId;


    @Column(columnDefinition = "VARCHAR(64) COMMENT '关键字'")
    private String fdKey;


    @Column(columnDefinition = "VARCHAR(64) COMMENT '模版类名'")
    private String fdModelName;



    @Column(columnDefinition = "VARCHAR(64) COMMENT '文件名称'")
    private String name;*/


    @Column(columnDefinition = "VARCHAR(64) COMMENT '大小'")
    private long size;

    @Column(columnDefinition = "VARCHAR(64) COMMENT 'uuid'")
    private String uid;

    @Column(columnDefinition = "VARCHAR(64) COMMENT '文件类型'")
    private String type;

    @Column(columnDefinition = "VARCHAR(1000) COMMENT '实际地址'")
    private String url;

    @Column(columnDefinition = "VARCHAR(64) COMMENT '类型'")
    private String status;


    @Transient
    private String fdKey;

}