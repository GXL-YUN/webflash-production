// ApiLog.java
package cn.enilu.log.bean.model;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "sys_api_log")
@Table(appliesTo = "sys_api_log", comment = "日志")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ApiLog  extends BaseEntity  {


    @Column(columnDefinition = "VARCHAR(200) COMMENT ''")
    private String requestId;

    @Column(columnDefinition = "VARCHAR(200) COMMENT ''")
    private String method;

    @Column(columnDefinition = "VARCHAR(2000) COMMENT ''")
    private String uri;

    @Column(columnDefinition = "VARCHAR(2000) COMMENT ''")
    private String queryParams;

    @Lob
    @Column()
    @Basic(fetch = FetchType.LAZY)
    private String requestBody;

    @Lob
    @Column()
    @Basic(fetch = FetchType.LAZY)
    private String responseBody;

    @Column()
    private Integer statusCode;

    @Column()
    private Long duration;

    @Column(columnDefinition = "VARCHAR(200) COMMENT ''")
    private String clientIp;

    @Column(columnDefinition = "VARCHAR(500) COMMENT ''")
    private String userAgent;

    @Column(columnDefinition = "VARCHAR(200) COMMENT ''")
    private String userId;


    @Column()
    private Boolean success = true;

    @Column(columnDefinition = "VARCHAR(2000) COMMENT ''")
    private String errorMessage;

    // 无参构造函数
    public ApiLog() {
    }

    // 全参构造函数
    public ApiLog(String requestId, String method, String uri, Long duration) {
        this.requestId = requestId;
        this.method = method;
        this.uri = uri;
        this.duration = duration;
    }
}