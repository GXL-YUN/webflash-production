package cn.enilu.flash.bean.entity.att;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.Embeddable;

// 修改AttachmentInfo类，添加@Embeddable注解
@Embeddable
@Data
public class AttachmentInfo {


        private String fdId;
        private String uid;
        private Long lastModified;
        private String lastModifiedDate;
        private String name;
        private Long size;
        private String type;
        private Integer percent;

        // 注意：不能包含复杂对象，如originFileObj
        // 如果需要存储复杂数据，可以序列化为JSON字符串
        private String originFileObjJson;

        // 辅助方法：将对象转换为JSON
        public void setOriginFileObj(Object obj) {
                if (obj == null) {
                        this.originFileObjJson = null;
                        return;
                }
                try {
                        ObjectMapper mapper = new ObjectMapper();
                        this.originFileObjJson = mapper.writeValueAsString(obj);
                } catch (Exception e) {
                        throw new RuntimeException("JSON转换失败", e);
                }
        }

        // 辅助方法：将JSON转换为对象
        public Object getOriginFileObj() {
                if (this.originFileObjJson == null) return null;
                try {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(this.originFileObjJson, Object.class);
                } catch (Exception e) {
                        throw new RuntimeException("JSON解析失败", e);
                }
        }
}

