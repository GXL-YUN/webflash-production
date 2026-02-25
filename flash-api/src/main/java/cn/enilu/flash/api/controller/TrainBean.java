package cn.enilu.flash.api.controller;

import cn.enilu.flash.api.utils.sql.bean.Alias;
import cn.enilu.flash.api.utils.sql.bean.BaseEntity;

import lombok.Data;

@Data
public class TrainBean extends BaseEntity {


        @Alias(value = "fdName")
        private String fdId;


        @Alias(value = "fdType")
        private String fdType;//课程名称


        @Alias(value = "fdNum")
        private String fdNum;  //培训人员账号

        @Override
        public String getTableName() {
            return "lua"; // 自定义表名
        }



}
