package cn.enilu.flash.api.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomDateUtil {
    /**
     *     使用示例：将10分成3份 List<Integer> parts = splitEqually(10, 3);
     * @param total
     * @param parts
     * @return
     */
    public static List<Integer> splitEqually(int total, int parts) {
        List<Integer> result = new ArrayList<>();
        int base = total / parts;
        int remainder = total % parts;

        for (int i = 0; i < parts; i++) {
            result.add(base + (i < remainder ? 1 : 0));
        }

        return result;
    }

    /**
     * 分解为随机数    List<Integer> randomParts = splitRandomly(10, 4);
     * @param total
     * @param parts
     * @return
     */
    public static List<Integer> splitRandomly(int total, int parts) {
        List<Integer> result = new ArrayList<>();
        Random random = new Random();
        int remaining = total;

        for (int i = 1; i < parts; i++) {
            int part = random.nextInt(remaining - (parts - i)) + 1;
            result.add(part);
            remaining -= part;
        }

        result.add(remaining);
        return result;
    }


    /**
     * 等比例拆分    List<Integer> ratioParts = splitByRatio(10, 1, 2, 1);
     */
    public static List<Integer> splitByRatio(int total, double... ratios) {
        List<Integer> result = new ArrayList<>();
        double sum = Arrays.stream(ratios).sum();
        int remaining = total;

        for (int i = 0; i < ratios.length - 1; i++) {
            int part = (int) Math.round(total * ratios[i] / sum);
            result.add(part);
            remaining -= part;
        }

        result.add(remaining);
        return result;
    }

    // 使用示例：按1:2:1的比例分解10


}
