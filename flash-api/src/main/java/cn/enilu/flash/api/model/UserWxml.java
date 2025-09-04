package cn.enilu.flash.api.model;


import lombok.Data;

@Data
public class UserWxml {

    private String wxmlCode;

    private String fdName;
    private String fdPhone;
    private String fdEmail;

    private String fdPassword;
}
