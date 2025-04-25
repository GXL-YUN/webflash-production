package cn.enilu.kmss.bean.entity;


import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.system.FileInfo;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 房屋表
 */
@Entity(name = "t_room_list")
@Table(appliesTo = "t_room_list", comment = "房屋")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RoomList extends BaseEntity {
    @Column(columnDefinition = "VARCHAR(200) COMMENT '房屋名称'")
    @NotNull(message = "房屋名称")
    private String fdName;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '房东姓名'")
    @NotNull(message = "房东姓名")
    private String fdRoomName;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '房东电话'")
    @NotNull(message = "房东电话")
    private String fdRoomPhone;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '房屋地址'")
    @NotNull(message = "房屋地址")
    private String fdAddres;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '房屋负责人'")
    @NotNull(message = "房屋负责人")
    private String fdPrincipal;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '整租合租'")
    @NotNull(message = "整租合租")
    private String fdIsWhole ;


    @Column(columnDefinition = "VARCHAR(200) COMMENT '合租房号位置ABCDE'")
    @NotNull(message = "合租房号位置ABCDE")
    private String fdAbcde;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '是否租出'")
    @NotNull(message = "是否租出")
    private String fdlease;


    @Column(columnDefinition = "VARCHAR(200) COMMENT '负责人电话'")
    @NotNull(message = "负责人电话")
    private String fdPrincipalPhone;


    @Column(columnDefinition = "VARCHAR(64) COMMENT '文章题图ID'")
    private String img;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '备注'")
    @NotNull(message = "备注")
    private String fdbz;


}
