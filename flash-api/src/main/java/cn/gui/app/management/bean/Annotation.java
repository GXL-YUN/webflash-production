package cn.gui.app.management.bean;

import java.sql.Date;
import java.time.LocalDateTime;

public class Annotation {

    private String fdMassage;
    private String fdId;

    private String fdMainId;

    private LocalDateTime fdCreateTime;

    public String getFdId() {
        return fdId;
    }

    public void setFdId(String fdId) {
        this.fdId = fdId;
    }

    public String getFdMassage() {
        return fdMassage;
    }

    public void setFdMassage(String fdMassage) {
        this.fdMassage = fdMassage;
    }

    public String getFdMainId() {
        return fdMainId;
    }

    public void setFdMainId(String fdMainId) {
        this.fdMainId = fdMainId;
    }

    public LocalDateTime getFdCreateTime() {
        return fdCreateTime;
    }

    public void setFdCreateTime(LocalDateTime fdCreateTime) {
        this.fdCreateTime = fdCreateTime;
    }
}
