package cn.gui.app.management;

import cn.gui.app.management.main.MainFrame;
import cn.gui.app.management.util.job.ScheduledTasks;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

@EnableScheduling
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ScheduledTasks s=new ScheduledTasks();

            s.taskWithCronExpression();
            MainFrame frame = new MainFrame();
            frame.setVisible(true);



        });
    }
}