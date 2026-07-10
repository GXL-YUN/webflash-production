package cn.enilu.pet.bean.model;


import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.bean.vo.SpringContextHolder;
import cn.enilu.flash.service.ServiceLocator;
import cn.enilu.flash.service.system.FileService;
import cn.enilu.flash.service.system.UserService;
import cn.enilu.pet.em.ADoptStatus;
import cn.enilu.project.bean.model.ProjectTack;
import cn.enilu.util.HostConfigUtil;
import cn.enilu.util.StringUtil;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 宠物领养 */
@Data
@Entity(name = "pet_demand_adopt")
@Table(appliesTo = "pet_demand_adopt", comment = "需求列表")
@EntityListeners(AuditingEntityListener.class)
public class Adopt extends BaseEntity {
    /**
     {
     "name": "小白",
     "type": "cat",
     "breed": "藏獒",
     "age": "10",
     "gender": "母",
     "weight": "4.5",
     "healthDetails": "健康状态",
     "requirements": [],
     "newRequirement": "领养要求",
     "description": "生活片习惯",
     "story": "故事",
     "contactName": "联系人",
     "contactPhone": "13720661531",
     "wechat": "微信号",
     "location": "地区",
     "images": [],
     "user_id": "dfa16cc3-2bb2-41e6-aac5-eb38f9e01fce"
     }
     */
    public Adopt() {
    }

    // 需求人ID
    @Column(name = "fd_user_id", columnDefinition = "VARCHAR(200) COMMENT '需求人ID'")
    @NotNull(message = "需求人ID不能为空")
    @JsonProperty("user_id")
    private String fdUserId;

    @Column(name = "fd_weight", columnDefinition = "VARCHAR(100) COMMENT '体重'")
    @JsonProperty("weight")
    private String fdWeight;



    @Column(name = "fd_health_details", columnDefinition = "VARCHAR(400) COMMENT '健康状态'")
    @JsonProperty("healthDetails")
    private String fdHealthDetails;



    // 详细描述
    @Column(name = "fd_story", columnDefinition = "TEXT COMMENT '故事'")
    @JsonProperty("story")
    private String fdStory;



    @Column(name = "fd_image_height", columnDefinition = "INT COMMENT '照片高度'")
    @JsonProperty("imageHeight")
    private Integer fdImageHeight;



    @Column(name = "fd_contact_phone", columnDefinition = "VARCHAR(200) COMMENT '手机号'")
    @JsonProperty("contactPhone")
    private String fdContactPhone;


    @Column(name = "fd_contact_name", columnDefinition = "VARCHAR(200) COMMENT '联系人'")
    @JsonProperty("contactName")
    private String fdContactName;



    @Column(name = "fd_wechat", columnDefinition = "VARCHAR(200) COMMENT '微信'")
    @JsonProperty("wechat")
    private String fdWechat;


    //    location: '北京市房山区',
    @Column(name = "fd_location", columnDefinition = "VARCHAR(200) COMMENT '地址'")
    @JsonProperty("location")
    private String fdLocation;


    //    publisher: '爱心家庭',
    @Column(name = "fd_publisher", columnDefinition = "VARCHAR(200) COMMENT '爱心家庭'")
    @JsonProperty("publisher")
    private String fdPublisher;

    //publishTime: '4天前'
    @Column(name = "fd_publish_time", columnDefinition = "DATETIME COMMENT '发布时间'")
    @JsonProperty("publishTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fdPublishTime;




    // 性别
    @Column(name = "fd_gender", columnDefinition = "VARCHAR(10) COMMENT '性别'")
    @JsonProperty("gender")
    private String fdGender;


    // 单据状态
    @Column(name = "fd_status", columnDefinition = "VARCHAR(20) DEFAULT 'available' COMMENT '单据状态'")
    @JsonProperty("status")
    private String fdStatus;

    // 年龄最小
    @Column(name = "fd_age", columnDefinition = "VARCHAR(50) COMMENT '年龄'")
    @JsonProperty("age")
    private String fdAge;

    // 名称
    @Column(name = "fd_name", columnDefinition = "VARCHAR(50) COMMENT '名称'")
    @JsonProperty("name")
    private String fdName;


    // 品种
    @Column(name = "fd_breed", columnDefinition = "VARCHAR(50) COMMENT '品种'")
    @JsonProperty("breed")
    private String fdBreed;


    // 宠物类型
    @Column(name = "fd_pet_type", columnDefinition = "VARCHAR(20) COMMENT '宠物类型'")
    @NotNull(message = "宠物类型不能为空")
    @JsonProperty("type")
    private String fdPetType;


    // 毛色颜色
    @Column(name = "fd_color", columnDefinition = "VARCHAR(50) COMMENT '毛色颜色'")
    @JsonProperty("color")
    private String fdColor;

    // 详细描述
    @Column(name = "fd_description", columnDefinition = "TEXT COMMENT '详细描述'")
    @JsonProperty("description")
    private String fdDescription;


    @Transient
    @JsonIgnore
    @JsonProperty("file")
    private   List<String> file;

    /**
     * 附件（临时字段，不持久化到数据库）
     */
    @Transient
    @JsonProperty("images")
    private   List<String> attacherList;
    // 添加自定义的序列化方法
    @JsonGetter("images")
    public List<String> getAttacherListForJson() {
        //获取bean
        FileService petService = SpringContextHolder.getBean(FileService.class);
        List<FileInfo>  list=petService.getListByIdFile(super.getFdId(),this.getClass().getName(),"attList");
        List<String> att=new ArrayList<>();
        for (FileInfo file:list){
            HostConfigUtil hostConfigUtil = SpringContextHolder.getBean(HostConfigUtil.class);

            att.add(hostConfigUtil.getHostProt()+"/static/runtime/tmp/"+file.getRealFileName());
        }
        return att;
    }

    /**
     * 数据库字段
     */
    @JsonIgnore
    @Column(name = "fd_requirements", columnDefinition = "VARCHAR(1000) COMMENT '领养要求'")
    private String fdRequirements;


    /**
     * 前端映射字段
     */
    @Transient
    @JsonProperty("requirements")
    private List<String> requirements=new ArrayList<>();

    @JsonGetter("requirements")
    public List<String>  getRequirements() {
        //获取bean

        return StringUtil.jsonToList(this.fdRequirements);
    }






    // 在保存前自动设置
    @PrePersist
    protected void onCreate() {
        if(this.requirements!=null){
            JSONArray arr=new JSONArray();
            for(String value: this.requirements){
                arr.put(value);
            }
          this.fdRequirements=arr.toString();
        }
        if (this.fdPublishTime == null) {
            this.fdPublishTime = new Date();
            this.fdImageHeight=100;
        }
        this.fdStatus= ADoptStatus.AVAILABLE.getCode();//available
    }



    @PreUpdate
    protected void onUpdate() {
        if(this.requirements!=null){
            this.fdRequirements=this.requirements.toString();
        }
    }

//
//    @JsonSetter("images")
//    public void requirements(List<String> images) {
//    }

//    // 添加自定义的序列化方法
//    @JsonGetter("requirements")
//    public List<String> getRequirementsForJson() {
//        List<String> arr=new ArrayList<>();
//        arr.add("2");
//        arr.add("2");
//        arr.add("2");
//        arr.add("2");
//        arr.add("2");
//        arr.add("2");
//        return arr;
//    }
}
