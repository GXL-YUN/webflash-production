package cn.enilu.pet.em;

public enum QuotesStstus {
    /**
     * 报价中  已采纳    已拒绝  撤回报价   已取消
     */
    INTHEQUOTATION("01", "报价中"),//in the quotation
    ADOPTED("02", "已采纳"),//Adopted
    REJECTED("03", "已拒绝"),//Rejected
    WITHDRAW("04", "撤回报价"),//withdraw
    CANAEL("05", "已取消");//cancel


    private final String code;
    private final String description;
    QuotesStstus(String code, String description) {
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
    public static QuotesStstus getByCode(String code) {
        for (QuotesStstus status : QuotesStstus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }


}
