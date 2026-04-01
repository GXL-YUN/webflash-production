package cn.enilu.flash.bean.entity;

import cn.enilu.flash.bean.entity.att.AttMainMassage;
import cn.enilu.flash.bean.entity.att.AttachmentInfo;
import cn.enilu.flash.bean.entity.system.FileInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Created  on 2019/1/8 0002.
 *
 * @author enilu
 */
@MappedSuperclass
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public abstract class BaseEntity implements Serializable {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "fd_id", updatable = false, nullable = false)
    private String fdId;

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "VARCHAR(50) COMMENT '项目编码'")
    private Long id;

    @CreationTimestamp
    @Column(name = "create_time", columnDefinition = "DATETIME COMMENT '创建时间/注册时间'", updatable = false)
    private Date createTime;
    @Column(name = "create_by", columnDefinition = "bigint COMMENT '创建人'", updatable = false)
    @CreatedBy
    private Long createBy;
    @UpdateTimestamp
    @Column(name = "modify_time", columnDefinition = "DATETIME COMMENT '最后更新时间'")
    private Date modifyTime;
    @LastModifiedBy
    @Column(name = "modify_by", columnDefinition = "bigint COMMENT '最后更新人'")
    private Long modifyBy;

    /**
     * 附件（临时字段，不持久化到数据库）
     */
    @Transient
    private   List<FileInfo> attacherList;
}
