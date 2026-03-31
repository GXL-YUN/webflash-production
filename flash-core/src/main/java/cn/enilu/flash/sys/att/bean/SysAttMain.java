package cn.enilu.flash.sys.att.bean;


import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.system.FileInfo;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件实际转化类
 */
@Data
@Entity
@Table(name = "sys_att_main")  // 正确的注解
@EntityListeners(AuditingEntityListener.class)
public class SysAttMain extends BaseEntity {

    @Column(columnDefinition = "VARCHAR(64) COMMENT '模版id'")
    private String fdModelId;


    @Column(columnDefinition = "VARCHAR(64) COMMENT '关键字'")
    private String fdKey;


    @Column(columnDefinition = "VARCHAR(64) COMMENT '模版类名'")
    private String fdModelName;



    @Column(columnDefinition = "VARCHAR(64) COMMENT '文件名称'")
    private String name;

    @Column(columnDefinition = "VARCHAR(64) COMMENT '文件id'")
    private String fd_file_id;



}
