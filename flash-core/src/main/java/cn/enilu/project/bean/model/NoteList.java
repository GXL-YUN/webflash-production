package cn.enilu.project.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 笔记列表
 */
@Data
@Entity(name = "sys_project_note_list")
@Table(appliesTo = "sys_project_note_list", comment = "笔记列表")
@EntityListeners(AuditingEntityListener.class)
public class NoteList  extends BaseEntity {

    @Column(columnDefinition = "VARCHAR(200) COMMENT '项目名称'")
    @NotNull(message = "项目名称不能为空")
    private String fdName;


    //1.2 使用 LONGTEXT类型（支持更大内容）
    @Column(columnDefinition = "LONGTEXT")
    private String richTextContent_long;




}
