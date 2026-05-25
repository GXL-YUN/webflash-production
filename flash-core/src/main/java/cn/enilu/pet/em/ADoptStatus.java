package cn.enilu.pet.em;

public enum ADoptStatus {

    AVAILABLE("available", "可领养"),
    RESERVED("reserved", "已预约"),
    ADOPTED("adopted", "已领养");


    private final String code;
    private final String description;
    ADoptStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // 根据code获取枚举
    public static ADoptStatus getByCode(String code) {
        for (ADoptStatus status : ADoptStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }


}
