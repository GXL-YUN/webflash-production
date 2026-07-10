package cn.enilu.sms.enu;

public enum SmsProvider {
    ALIYUN("aliyun", "阿里云"),
    TENCENT("tencent", "腾讯云"),
    HUAWEI("huawei", "华为云"),
    YUNPIAN("yunpian", "云片");

    private final String code;
    private final String name;

    SmsProvider(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
}
