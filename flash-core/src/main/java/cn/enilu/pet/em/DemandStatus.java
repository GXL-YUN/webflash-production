package cn.enilu.pet.em;

public enum DemandStatus {

    /**
     * 草稿
     */
    DRAFT("1", "待报价"),

    /**
     * 确认报价
     */
    PUBLISHED("2", "确认报价"),

    /**
     * 已下架
     */
    OFFLINE("3", "已下架"),

    /**
     * 报价中
     */
    COMPLETED("4", "报价中"),


    /**
     * 报价中affirmDate
     */
    AFFIRMDATE("4", "已确认报价"),
    /**
     * 已取消
     */
    CANCELLED("5", "已取消"),

    /**
     * 已发货
     */
    SHIPMENTS("6", "已发货"),


    /**
     * 已收货
     */
    TAKEDELIVERYOFGOODS("7", "已收货");


    private final String code;
    private final String description;

    DemandStatus(String code, String description) {
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
    public static DemandStatus getByCode(String code) {
        for (DemandStatus status : DemandStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
