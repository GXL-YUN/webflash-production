package cn.enilu.kmss.bean.entity;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 公告表
 */
@Entity(name = "t_announcement_list")
@Table(appliesTo = "t_announcement_list", comment = "房源公告")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AnnouncementBean  extends BaseEntity {


    @Column(columnDefinition = "VARCHAR(200) COMMENT '房源公告信息'")
    @NotNull(message = "公告信息")
    private String fdMassage;


    @Column(columnDefinition = "VARCHAR(200) COMMENT '房源公告名称'")
    @NotNull(message = "公告名称")
    private String fdName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATETIME COMMENT '房源公告结束时间'")
    private Date fdEndDate;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '房源id'")
    @NotNull(message = "房源id")
    private String fdRoomId;

}
