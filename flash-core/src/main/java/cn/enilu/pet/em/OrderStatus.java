package cn.enilu.pet.em;

/**
 * 0 待支付 1 已支付 2 已取消
 */
public enum OrderStatus {

    WITHPAYMENT("0", "待支付"),
    PAYMENT("1", "已支付"),
    CANCALAYMENT("2", "已取消");
    private final String code;
    private final String description;
    OrderStatus(String code, String description) {
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
    public static OrderStatus getByCode(String code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }


}

