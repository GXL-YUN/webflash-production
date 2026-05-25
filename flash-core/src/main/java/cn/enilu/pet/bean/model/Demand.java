package cn.enilu.pet.bean.model;

import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.project.bean.model.ProjectRisk;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 需求管理
 */
@Data
@Entity(name = "pet_demand_list")
@Table(appliesTo = "pet_demand_list", comment = "需求列表")
@EntityListeners(AuditingEntityListener.class)
public class Demand extends BaseEntity {

    // 宠物类型
    @Column(name = "fd_pet_type", columnDefinition = "VARCHAR(20) COMMENT '宠物类型'")
    @NotNull(message = "宠物类型不能为空")
    @JsonProperty("pet_type")
    private String fdPetType;

    // 品种
    @Column(name = "fd_breed", columnDefinition = "VARCHAR(50) COMMENT '品种'")
    @JsonProperty("breed")
    private String fdBreed;

    // 性别
    @Column(name = "fd_gender", columnDefinition = "VARCHAR(10) COMMENT '性别'")
    @JsonProperty("gender")
    private String fdGender;

    // 毛色颜色
    @Column(name = "fd_color", columnDefinition = "VARCHAR(50) COMMENT '毛色颜色'")
    @JsonProperty("color")
    private String fdColor;

    // 年龄最小
    @Column(name = "fd_age_min", columnDefinition = "INT COMMENT '年龄最小'")
    @JsonProperty("age_min")
    private Integer fdAgeMin;

    // 年龄最大 - 修复：这里原来是错误的字段名 'c'
    @Column(name = "fd_age_max", columnDefinition = "INT COMMENT '年龄最大'")
    @JsonProperty("age_max")
    private Integer fdAgeMax;

    // 是否打疫苗
    @Column(name = "fd_vaccine_required", columnDefinition = "BOOLEAN DEFAULT true COMMENT '是否打疫苗'")
    @JsonProperty("vaccine_required")
    private Boolean fdVaccineRequired;

    // 最低预算
    @Column(name = "fd_budget_min", columnDefinition = "DECIMAL(10,2) COMMENT '最低预算'")
    @JsonProperty("budget_min")
    private BigDecimal fdBudgetMin;

    // 最高预算
    @Column(name = "fd_budget_max", columnDefinition = "DECIMAL(10,2) COMMENT '最高预算'")
    @JsonProperty("budget_max")
    private BigDecimal fdBudgetMax;

    // 地址ID
    @Column(name = "fd_address_id", columnDefinition = "VARCHAR(200) COMMENT '地址ID'")
    @JsonProperty("address_id")
    private String fdAddressId;

    // 详细描述
    @Column(name = "fd_description", columnDefinition = "TEXT COMMENT '详细描述'")
    @JsonProperty("description")
    private String fdDescription;

    // 单据状态   单据默认报价中
    @Column(name = "fd_status", columnDefinition = "VARCHAR(20) DEFAULT '1' COMMENT '单据状态'")
    @JsonProperty("status")
    private String fdStatus;


    @Column(name = "fd_down_status", columnDefinition = "VARCHAR(20) DEFAULT '2' COMMENT '下架状态'")
    @JsonProperty("fd_down_status")
    private String fdDownStatus;

    // 报价数量
    @Column(name = "fd_quotes_count", columnDefinition = "INT DEFAULT 0 COMMENT '报价数量'")
    @JsonProperty("quotes_count")
    private Integer fdQuotesCount;

    // 创建时间
    @Column(name = "fd_created_at", columnDefinition = "DATETIME COMMENT '创建时间'")
    @JsonProperty("created_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fdCreatedAt;

    // 更新时间
    @Column(name = "fd_updated_at", columnDefinition = "DATETIME COMMENT '更新时间'")
    @JsonProperty("fd_updated_c")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fdUpdatedAt;

    // 需求人ID
    @Column(name = "fd_user_id", columnDefinition = "VARCHAR(200) COMMENT '需求人ID'")
    @NotNull(message = "需求人ID不能为空")
    @JsonProperty("user_id")
    private String fdUserId;

    /**
     * 报价记录
     */
    @OneToMany(
            mappedBy = "fdDemand",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonProperty("quotes")
    private List<Quotes> fdQuotesList = new ArrayList<>();



    /**
     * 附件（临时字段，不持久化到数据库）
     */
//    @Transient
//    @JsonProperty("attacherList")
//    private   List<FileInfo> attacherList;
//    // 添加自定义的序列化方法
//    @JsonGetter("attacherList")
//    public List<FileInfo> getAttacherListForJson() {
//        //获取bean
//        FileService petService = SpringContextHolder.getBean(FileService.class);
//        List<FileInfo>  list=petService.getListByIdFile(super.getFdId(),this.getClass().getName(),"fdList");
//        return list;
//    }


    // 在保存前自动设置
    @PrePersist
    protected void onCreate() {
        if (this.fdStatus == null) {
            this.fdStatus = "pending";
        }
        if (this.fdQuotesCount == null) {
            this.fdQuotesCount = 0;
        }
        if (this.fdVaccineRequired == null) {
            this.fdVaccineRequired = true;
        }
        if (this.fdCreatedAt == null) {
            this.fdCreatedAt = new Date();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fdUpdatedAt = new Date();
    }
}