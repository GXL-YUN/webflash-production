package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 宠物档案  pets
 */
@Data
@Entity(name = "pet_demand_pets")
@Table(appliesTo = "pet_demand_pets", comment = "宠物档案")
@EntityListeners(AuditingEntityListener.class)
public class PetsList extends BaseEntity {


    /**
     * id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     * user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     * name VARCHAR(50) NOT NULL,
     * pet_type VARCHAR(20) NOT NULL CHECK (pet_type IN ('cat', 'dog')),
     * breed VARCHAR(50) NOT NULL,
     * gender VARCHAR(10) NOT NULL CHECK (gender IN ('male', 'female')),
     * color VARCHAR(50),
     * description TEXT
     * <p>
     * birthday TIMESTAMPTZ,
     * photo_url TEXT,
     * weight NUMERIC(10, 2),
     * sterilized BOOLEAN,
     * chip_number VARCHAR(50),
     * personality VARCHAR(500),
     * vaccine_records JSONB,
     * deworming_records JSONB,
     * ,
     */
    // 需求人ID
    @Column(name = "fd_user_id", columnDefinition = "VARCHAR(200) COMMENT '需求人ID'")
    @NotNull(message = "需求人ID不能为空")
    @JsonProperty("user_id")
    private String fdUserId;


    @Column(name = "fd_name", columnDefinition = "VARCHAR(200) COMMENT '名称'")
    @JsonProperty("name")
    private String fdNme;


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


    // 详细描述
    @Column(name = "fd_description", columnDefinition = "TEXT COMMENT '详细描述'")
    @JsonProperty("description")
    private String fdDescription;


   //      *   birthday TIMESTAMPTZ,

    @CreationTimestamp
    @Column(name = "fd_birthday", columnDefinition = "DATETIME COMMENT '生日'")
    @JsonProperty("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fBbirthday;


    //     *   photo_url TEXT,
    @Column(name = "fd_photo_url", columnDefinition = "VARCHAR(50) COMMENT '图片路径'")
    @JsonProperty("photo_url")
    private String fdPhotoUrl;

   //     *   weight NUMERIC(10, 2),

    // 体重
    @Column(name = "fd_weight", columnDefinition = "DECIMAL(10,2) COMMENT '最高预算'")
    @JsonProperty("weight")
    private BigDecimal fdWeight;
   //     *   sterilized BOOLEAN,


    //     *   chip_number VARCHAR(50),
   // 芯片编号
    @Column(name = "fd_chip_number", columnDefinition = "VARCHAR(50) COMMENT '芯片编号'")
    @JsonProperty("chip_number")
    private String fdChipNumber;
    //     *   personality VARCHAR(500),
   // 性格特点
    @Column(name = "fd_personality", columnDefinition = "VARCHAR(50) COMMENT '性格特点'")
    @JsonProperty("personality")
    private String fdPersonality;
   //     *   vaccine_records JSONB,
   //     *   deworming_records JSONB,

}