import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    // 每5秒执行一次
    @Scheduled(cron = "*/5 * * * * ?")
    public void taskWithCronExpression() {
        System.out.println("执行定时任务: " + System.currentTimeMillis());
    }

    // 固定速率，每5秒执行一次（不考虑任务执行时间）
    @Scheduled(fixedRate = 5000)
    public void taskWithFixedRate() {
        System.out.println("固定速率任务: " + System.currentTimeMillis());
    }

    // 固定延迟，任务完成后延迟5秒再执行
    @Scheduled(fixedDelay = 5000)
    public void taskWithFixedDelay() {
        System.out.println("固定延迟任务: " + System.currentTimeMillis());
    }
}