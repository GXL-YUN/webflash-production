package cn.enilu.sms.util;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class VerifyCodeGenerator {

    private static final String BASE_CHAR =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 去 O0I1
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成核销码
     */
    public static String generate(String bizType) {
        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
        String random = randomStr(8);
        return "HX" + date + bizType + random;
    }

    private static String randomStr(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(BASE_CHAR.charAt(RANDOM.nextInt(BASE_CHAR.length())));
        }
        return sb.toString();
    }



    private static final int CODE_LENGTH = 12;


    /**
     *  基础核销码生成（推荐）
     * @param prefix
     * @return
     */
    public static String generateVerifyCode(String prefix) {
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(BASE_CHAR.length());
            sb.append(BASE_CHAR.charAt(index));
        }

        return prefix + sb;
    }




    public static void main(String[] args) {
        VerifyCodeGenerator  v=new VerifyCodeGenerator();
        System.out.println( v.generate("1111"));
    }
}