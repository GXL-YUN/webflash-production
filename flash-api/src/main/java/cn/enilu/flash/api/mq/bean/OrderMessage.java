package cn.enilu.flash.api.mq.bean;

import lombok.Data;

@Data
public class OrderMessage {


    private String action;

    private Object data;
}
