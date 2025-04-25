package cn.enilu.flash.api.model;

public class UserWxml {

    private String wxmlCode;

    private String fdName;


    private String fdPhone;

    public String getFdPhone() {
        return fdPhone;
    }

    public void setFdPhone(String fdPhone) {
        this.fdPhone = fdPhone;
    }


    private String fdEmail;

    public String getFdEmail() {
        return fdEmail;
    }

    public void setFdEmail(String fdEmail) {
        this.fdEmail = fdEmail;
    }

    public String getWxmlCode() {
        return wxmlCode;
    }

    public String getFdName() {
        return fdName;
    }

    public String getFdPassword() {
        return fdPassword;
    }

    private String fdPassword;

    public void setWxmlCode(String wxmlCode) {
        this.wxmlCode = wxmlCode;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public void setFdPassword(String fdPassword) {
        this.fdPassword = fdPassword;
    }
}
