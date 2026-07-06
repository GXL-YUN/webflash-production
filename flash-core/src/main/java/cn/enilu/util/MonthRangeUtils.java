package cn.enilu.util;


import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class MonthRangeUtils {

    /**
     * 获取月份区间的结果对象
     */
    public static class MonthRangeResult {
        private String currentMonth;  // 2023-03
        private String currentStart;  // 2023-03-01
        private String currentEnd;    // 2023-03-31

        private String previousMonth; // 2023-02
        private String previousStart; // 2023-02-01
        private String previousEnd;   // 2023-02-28

        private String nextMonth;    // 2023-04
        private String nextStart;     // 2023-04-01
        private String nextEnd;       // 2023-04-30

        // 构造函数、getter、setter
        public MonthRangeResult() {}

        // toString 方法
        @Override
        public String toString() {
            return "当前月份 (" + currentMonth + "): " + currentStart + " ~ " + currentEnd + "\n" +
                    "前一个月 (" + previousMonth + "): " + previousStart + " ~ " + previousEnd + "\n" +
                    "后一个月 (" + nextMonth + "): " + nextStart + " ~ " + nextEnd;
        }

        // 所有 getter 和 setter
        public String getCurrentMonth() { return currentMonth; }
        public void setCurrentMonth(String currentMonth) { this.currentMonth = currentMonth; }

        public String getCurrentStart() { return currentStart; }
        public void setCurrentStart(String currentStart) { this.currentStart = currentStart; }

        public String getCurrentEnd() { return currentEnd; }
        public void setCurrentEnd(String currentEnd) { this.currentEnd = currentEnd; }

        public String getPreviousMonth() { return previousMonth; }
        public void setPreviousMonth(String previousMonth) { this.previousMonth = previousMonth; }

        public String getPreviousStart() { return previousStart; }
        public void setPreviousStart(String previousStart) { this.previousStart = previousStart; }

        public String getPreviousEnd() { return previousEnd; }
        public void setPreviousEnd(String previousEnd) { this.previousEnd = previousEnd; }

        public String getNextMonth() { return nextMonth; }
        public void setNextMonth(String nextMonth) { this.nextMonth = nextMonth; }

        public String getNextStart() { return nextStart; }
        public void setNextStart(String nextStart) { this.nextStart = nextStart; }

        public String getNextEnd() { return nextEnd; }
        public void setNextEnd(String nextEnd) { this.nextEnd = nextEnd; }
    }

    /**
     * 根据日期字符串获取月份区间
     * @param dateStr 日期字符串，支持 yyyy-M-d, yyyy/MM/dd, yyyy-MM-dd 格式
     * @return MonthRangeResult 对象
     */
    public static MonthRangeResult getMonthRange(String dateStr) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("[yyyy-M-d]")
                .appendPattern("[yyyy/MM/dd]")
                .appendPattern("[yyyy-MM-dd]")
                .toFormatter(Locale.CHINA);

        LocalDate date = LocalDate.parse(dateStr, formatter);

        return getMonthRange(date);
    }

    /**
     * 根据LocalDate获取月份区间
     */
    public static MonthRangeResult getMonthRange(LocalDate date) {
        MonthRangeResult result = new MonthRangeResult();

        YearMonth currentMonth = YearMonth.from(date);
        YearMonth previousMonth = currentMonth.minusMonths(1);
        YearMonth nextMonth = currentMonth.plusMonths(1);

        // 设置月份标识
        result.setCurrentMonth(currentMonth.toString());
        result.setPreviousMonth(previousMonth.toString());
        result.setNextMonth(nextMonth.toString());

        // 设置日期区间
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        result.setCurrentStart(currentMonth.atDay(1).format(dateFormatter));
        result.setCurrentEnd(currentMonth.atEndOfMonth().format(dateFormatter));

        result.setPreviousStart(previousMonth.atDay(1).format(dateFormatter));
        result.setPreviousEnd(previousMonth.atEndOfMonth().format(dateFormatter));

        result.setNextStart(nextMonth.atDay(1).format(dateFormatter));
        result.setNextEnd(nextMonth.atEndOfMonth().format(dateFormatter));

        return result;
    }

    /**
     * 获取当前日期的月份区间
     */
    public static MonthRangeResult getCurrentMonthRange() {
        return getMonthRange(LocalDate.now());
    }

    public static void main(String[] args) {
        // 测试
        System.out.println("=== 测试 2023-3-28 ===");
        MonthRangeResult result1 = getMonthRange("2023-3-28");
        System.out.println(result1);

        System.out.println("\n=== 测试 2026-4-28 ===");
        MonthRangeResult result2 = getMonthRange("2026-4-28");
        System.out.println(result2);

        System.out.println("\n=== 测试当前日期 ===");
        MonthRangeResult result3 = getCurrentMonthRange();
        System.out.println(result3);

        // 获取具体值
        System.out.println("\n=== 获取特定值 ===");
        System.out.println("当前月份开始日期: " + result1.getCurrentStart());
        System.out.println("前一个月结束日期: " + result1.getPreviousEnd());
        System.out.println("后一个月: " + result1.getNextMonth());
    }
}