package cn.enilu.mq.bean;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity(name = "mq_main_log")
@org.hibernate.annotations.Table(
        appliesTo = "mq_main_log",
        comment = "mq日志记录表"
)
@Data
@EntityListeners(AuditingEntityListener.class)
public class MqMainLog extends BaseEntity {


    @Column(name = "fd_requestId", columnDefinition = "VARCHAR(200) COMMENT '业务id'")
    private String fdRequestId;

    @Column(name = "fd_service",columnDefinition = "VARCHAR(200) COMMENT '服务Bean类名'")
    private String fdService;

    @Lob
    @Column(name = "fd_requestBody")
    @Basic(fetch = FetchType.LAZY)
    private String fdRequestBody;


    @Column(name = "fd_status")
    @ColumnDefault("1")   //状态: 0投递中 1投递成功 2投递失败 3已消费
    private Integer fdStatus;

}