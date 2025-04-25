package cn.enilu.flash.api.main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.List;


/**
 * 日常管理模块小工具生成ui界面对于日常工作进行管理
 */
public class DesktopCalendar extends JFrame {
    private JLabel monthLabel;
    private JPanel calendarPanel;
    private YearMonth currentYearMonth;
    private JButton toggleButton;
    private boolean isMinimized = false;
    private JPanel minimizedPanel;
    private javax.swing.Timer dateUpdateTimer;
    private Map<LocalDate, List<String>> scheduleMap = new HashMap<>();
    private JLabel miniDateLabel;

    // 颜色定义
    private final Color MAIN_BG_COLOR = new Color(245, 245, 245);
    private final Color HEADER_COLOR = new Color(70, 130, 180);
    private final Color TODAY_COLOR = new Color(220, 80, 80);
    private final Color WEEKEND_COLOR = new Color(150, 150, 150);
    private final Color WEEKDAY_COLOR = new Color(80, 80, 80);
    private final Color HOVER_COLOR = new Color(230, 240, 255);

    public DesktopCalendar() {
        setTitle("桌面日历小工具");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setBackground(new Color(255, 255, 255, 230));
        setShape(new RoundRectangle2D.Double(0, 0, 450, 500, 20, 20));

        currentYearMonth = YearMonth.now();

        createUI();
        setupDrag();
        setupAutoUpdate();
    }

    private void createUI() {
        setLayout(new BorderLayout(0, 10));
        getContentPane().setBackground(MAIN_BG_COLOR);
        //setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // 最小化/展开按钮
        toggleButton = createStyledButton("−", 20);
        toggleButton.addActionListener(e -> toggleMinimized());

        // 月份导航
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JButton prevButton = createStyledButton("←", 16);
        prevButton.addActionListener(e -> changeMonth(-1));

        JButton nextButton = createStyledButton("→", 16);
        nextButton.addActionListener(e -> changeMonth(1));

        monthLabel = new JLabel();
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        monthLabel.setForeground(HEADER_COLOR);

        navPanel.add(prevButton, BorderLayout.WEST);
        navPanel.add(nextButton, BorderLayout.EAST);
        navPanel.add(monthLabel, BorderLayout.CENTER);

        // 关闭按钮
        JButton closeButton = createStyledButton("×", 16);
        closeButton.addActionListener(e -> System.exit(0));
        closeButton.setForeground(new Color(200, 50, 50));

        topPanel.add(toggleButton, BorderLayout.WEST);
        topPanel.add(navPanel, BorderLayout.CENTER);
        topPanel.add(closeButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 星期标题
        JPanel weekdaysPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        weekdaysPanel.setOpaque(false);
        String[] weekdays = {"日", "一", "二", "三", "四", "五", "六"};
        for (String day : weekdays) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("微软雅黑", Font.BOLD, 14));
            label.setForeground(day.equals("日") || day.equals("六") ? WEEKEND_COLOR : WEEKDAY_COLOR);
            weekdaysPanel.add(label);
        }
        add(weekdaysPanel, BorderLayout.CENTER);

        // 日历面板
        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        calendarPanel.setOpaque(false);
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(calendarPanel, BorderLayout.CENTER);

        // 最小化面板
        minimizedPanel = new JPanel(new BorderLayout());
        minimizedPanel.setOpaque(false);
        miniDateLabel = new JLabel("", SwingConstants.CENTER);
        miniDateLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        miniDateLabel.setForeground(HEADER_COLOR);
        minimizedPanel.add(miniDateLabel, BorderLayout.CENTER);

        JButton expandButton = createStyledButton("+", 14);
        expandButton.addActionListener(e -> toggleMinimized());
        minimizedPanel.add(expandButton, BorderLayout.EAST);

        // 底部面板 - 日程显示
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addScheduleButton = createStyledButton("添加日程", 14);
        addScheduleButton.addActionListener(e -> showAddScheduleDialog());
        bottomPanel.add(addScheduleButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        updateCalendar();
    }

    private JButton createStyledButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setForeground(HEADER_COLOR);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(TODAY_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(HEADER_COLOR);
            }
        });

        return button;
    }

    private void setupDrag() {
        addMouseMotionListener(new MouseMotionAdapter() {
            Point origin;
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    Point p = getLocation();
                    setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
                }
            }
            public void mousePressed(MouseEvent e) {
                origin = e.getPoint();
            }
        });
    }

    private void setupAutoUpdate() {
        dateUpdateTimer = new javax.swing.Timer(1000 * 60, e -> {
            LocalDate now = LocalDate.now();
            if (currentYearMonth.equals(YearMonth.from(now))) {
                updateCalendar();
            }
            if (isMinimized) {
                miniDateLabel.setText(now.format(DateTimeFormatter.ofPattern("MM/dd")));
            }
        });
        dateUpdateTimer.start();
    }

    private void toggleMinimized() {
        isMinimized = !isMinimized;

        if (isMinimized) {
            // 切换到最小化模式
            remove(calendarPanel);
            getContentPane().add(minimizedPanel, BorderLayout.CENTER);
            toggleButton.setText("+");
            setSize(150, 50);

            // 更新最小化面板显示
            LocalDate today = LocalDate.now();
            miniDateLabel.setText(today.format(DateTimeFormatter.ofPattern("MM/dd")));
        } else {
            // 切换到完整模式
            remove(minimizedPanel);
            getContentPane().add(calendarPanel, BorderLayout.CENTER);
            toggleButton.setText("−");
            setSize(450, 500);
        }

        revalidate();
        repaint();
    }

    private void changeMonth(int months) {
        currentYearMonth = currentYearMonth.plusMonths(months);
        updateCalendar();
    }

    private void updateCalendar() {
        monthLabel.setText(currentYearMonth.format(DateTimeFormatter.ofPattern("yyyy年 M月")));
        calendarPanel.removeAll();

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        DayOfWeek firstDayOfWeek = firstOfMonth.getDayOfWeek();
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // 添加空白格子
        for (int i = 0; i < firstDayOfWeek.getValue() % 7; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // 添加日期
        LocalDate today = LocalDate.now();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = currentYearMonth.atDay(day);
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setOpaque(false);

            JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            dayLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

            // 设置日期颜色
            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY ||
                    currentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                dayLabel.setForeground(WEEKEND_COLOR);
            } else {
                dayLabel.setForeground(WEEKDAY_COLOR);
            }

            // 高亮今天
            if (currentDate.equals(today)) {
                dayLabel.setOpaque(true);
                dayLabel.setBackground(TODAY_COLOR);
                dayLabel.setForeground(Color.WHITE);
                dayLabel.setBorder(BorderFactory.createLineBorder(TODAY_COLOR.darker(), 2));
            }

            // 添加日程标记
            if (scheduleMap.containsKey(currentDate)) {
                JLabel scheduleDot = new JLabel("•");
                scheduleDot.setForeground(new Color(50, 150, 50));
                scheduleDot.setFont(new Font("Arial", Font.BOLD, 20));
                scheduleDot.setHorizontalAlignment(SwingConstants.CENTER);
                dayPanel.add(scheduleDot, BorderLayout.SOUTH);
            }

            dayPanel.add(dayLabel, BorderLayout.CENTER);

            // 添加点击事件查看日程
            dayPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    showScheduleForDate(currentDate);
                }
                public void mouseEntered(MouseEvent e) {
                    dayPanel.setOpaque(true);
                    dayPanel.setBackground(HOVER_COLOR);
                    dayPanel.repaint();
                }
                public void mouseExited(MouseEvent e) {
                    dayPanel.setOpaque(false);
                    dayPanel.repaint();
                }
            });

            calendarPanel.add(dayPanel);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void showAddScheduleDialog() {
        JDialog dialog = new JDialog(this, "添加日程", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(MAIN_BG_COLOR);
        dialog.setResizable(false);

        JPanel contentPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setOpaque(false);

        // 日期选择
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setOpaque(false);
        JLabel dateLabel = new JLabel("日期:");
        JTextField dateField = new JTextField(LocalDate.now().toString());
        datePanel.add(dateLabel, BorderLayout.WEST);
        datePanel.add(dateField, BorderLayout.CENTER);

        // 时间选择
        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.setOpaque(false);
        JLabel timeLabel = new JLabel("时间:");
        JTextField timeField = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timePanel.add(timeLabel, BorderLayout.WEST);
        timePanel.add(timeField, BorderLayout.CENTER);

        // 日程内容
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setOpaque(false);
        JLabel descLabel = new JLabel("内容:");
        JTextField descField = new JTextField();
        descPanel.add(descLabel, BorderLayout.WEST);
        descPanel.add(descField, BorderLayout.CENTER);

        contentPanel.add(datePanel);
        contentPanel.add(timePanel);
        contentPanel.add(descPanel);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                String schedule = timeField.getText() + ": " + descField.getText();

                scheduleMap.computeIfAbsent(date, k -> new ArrayList<>()).add(schedule);
                updateCalendar();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "日期格式错误，请使用YYYY-MM-DD格式", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showScheduleForDate(LocalDate date) {
        List<String> schedules = scheduleMap.getOrDefault(date, Collections.emptyList());

        JDialog dialog = new JDialog(this, date.toString() + " 的日程", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG_COLOR);

        JTextArea scheduleArea = new JTextArea();
        scheduleArea.setEditable(false);
        scheduleArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        scheduleArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (schedules.isEmpty()) {
            scheduleArea.setText("这一天没有日程安排");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : schedules) {
                sb.append("• ").append(s).append("\n");
            }
            scheduleArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(scheduleArea);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton addButton = new JButton("添加");
        addButton.addActionListener(e -> {
            dialog.dispose();
            showAddScheduleDialog();
        });

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> {
            if (!schedules.isEmpty()) {
                String[] options = schedules.toArray(new String[0]);
                String selected = (String) JOptionPane.showInputDialog(
                        dialog,
                        "选择要删除的日程:",
                        "删除日程",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (selected != null) {
                    schedules.remove(selected);
                    if (schedules.isEmpty()) {
                        scheduleMap.remove(date);
                    }
                    updateCalendar();
                    dialog.dispose();
                    showScheduleForDate(date);
                }
            }
        });

        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addButton);
        if (!schedules.isEmpty()) {
            buttonPanel.add(deleteButton);
        }
        buttonPanel.add(closeButton);

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            DesktopCalendar calendar = new DesktopCalendar();
            calendar.setVisible(true);
        });
    }
}