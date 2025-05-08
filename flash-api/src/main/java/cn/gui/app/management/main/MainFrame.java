package cn.gui.app.management.main;

import cn.gui.app.management.bean.Task;
import cn.gui.app.management.dao.TaskDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
    private TaskDAO taskDAO;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField taskNameField;
    private JTextArea descriptionArea;

    private JComboBox<String> comboBox;

    public MainFrame() {
        taskDAO = new TaskDAO();
        initializeUI();
        loadTasks();
    }

    private void initializeUI() {
        setTitle("工作任务监控系统");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 任务列表表格
        String[] columnNames = {"ID", "任务名称", "描述", "开始时间", "结束时间", "用时(分钟)", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable = new JTable(tableModel);


        // 设置自定义渲染器
        taskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                // 获取状态列的值（假设状态是第2列，索引从0开始）
                String status = (String) table.getModel().getValueAt(row, 6);

                if (!isSelected) {
                    switch (status) {
                        case "未开始":
                            c.setBackground(new Color(255, 255, 200)); // 浅黄色
                            break;
                        case "进行中":
                            c.setBackground(new Color(200, 255, 200)); // 浅绿色
                            break;
                        case "已完成":
                            c.setBackground(new Color(220, 220, 220)); // 浅灰色
                            break;
                        default:
                            c.setBackground(table.getBackground());
                    }
                }

                return c;
            }
        });


        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(taskTable);

        // 任务详情面板
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createTitledBorder("任务详情"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        detailPanel.add(new JLabel("任务名称:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        taskNameField = new JTextField(60);
        detailPanel.add(taskNameField, gbc);




        String[] items = {"全部","已完成", "未开始", "进行中"};
       comboBox = new JComboBox<>(items);
        gbc.gridx = 3;
        gbc.gridwidth = 3;
        detailPanel.add(comboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        detailPanel.add(new JLabel("描述:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        descriptionArea = new JTextArea(5, 60);
        descriptionArea.setLineWrap(true);
        detailPanel.add(new JScrollPane(descriptionArea), gbc);


        JButton select = new JButton("查询");
        select.addActionListener(e -> select());
        gbc.gridx = 3;
        detailPanel.add(select, gbc);


        JButton collect = new JButton("汇总");
        collect.addActionListener(e -> getNewDate());
        gbc.gridx = 4;
        detailPanel.add(collect, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 5, 5));

        JButton addButton = new JButton("添加任务");
        addButton.addActionListener(e -> addTask());

        JButton startButton = new JButton("开始任务");
        startButton.addActionListener(e -> startSelectedTask());

        JButton endButton = new JButton("结束任务");
        endButton.addActionListener(e -> endSelectedTask());

        JButton updateButton = new JButton("更新任务");
        updateButton.addActionListener(e -> updateSelectedTask());

        JButton deleteButton = new JButton("删除任务");
        deleteButton.addActionListener(e -> deleteSelectedTask());

        buttonPanel.add(addButton);
        buttonPanel.add(startButton);
        buttonPanel.add(endButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // 添加组件到主面板
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(detailPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 表格选择监听器
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displayTaskDetails(selectedRow);
                }
            }
        });

        add(mainPanel);
    }

    /**
     * 获取今日日报
     */
    private void getNewDate() {
        List<Task> tasks = taskDAO.getNewDate();
        StringBuilder date=new StringBuilder("");
        Map<String,String> map=new HashMap<>();
        map.put("NOT_STARTED","未开始");
        map.put("COMPLETED","已完成");
        map.put("IN_PROGRESS","进行中");
        for (Task task:tasks){
            date.append("任务名称："+task.getTaskName()+" 任务描述："+task.getDescription().replaceAll("\\s*|\t|\r|\n", "")+" 是否完成："+map.get(task.getStatus())+"\n");
        }
        descriptionArea.setText(date.toString());
        System.out.println("");
    }


    private void loadTasks() {
        List<Task> tasks = taskDAO.getAllTasks();
        tableModel.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Task task : tasks) {
            Object[] rowData = {
                    task.getId(),
                    task.getTaskName(),
                    task.getDescription(),
                    task.getStartTime() != null ? task.getStartTime().format(formatter) : "",
                    task.getEndTime() != null ? task.getEndTime().format(formatter) : "",
                    task.getTotalMinutes(),
                    convertStatus(task.getStatus())

            };
            tableModel.addRow(rowData);
        }
    }

    private void select() {
        String taskName = taskNameField.getText().trim();

        String selected = (String) comboBox.getSelectedItem();
       List<Task> tasks = taskDAO.getByName(taskName,selected.trim());
        tableModel.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Task task : tasks) {
            Object[] rowData = {
                    task.getId(),
                    task.getTaskName(),
                    task.getDescription(),
                    task.getStartTime() != null ? task.getStartTime().format(formatter) : "",
                    task.getEndTime() != null ? task.getEndTime().format(formatter) : "",
                    task.getTotalMinutes(),
                    convertStatus(task.getStatus())

            };
            tableModel.addRow(rowData);
        }
    }

    private String convertStatus(String status) {
        switch (status) {
            case "NOT_STARTED": return "未开始";
            case "IN_PROGRESS": return "进行中";
            case "COMPLETED": return "已完成";
            default: return status;
        }
    }

    private void displayTaskDetails(int rowIndex) {
        int id = (int) tableModel.getValueAt(rowIndex, 0);
        String taskName = (String) tableModel.getValueAt(rowIndex, 1);
        String description = (String) tableModel.getValueAt(rowIndex, 2);

        taskNameField.setText(taskName);
        descriptionArea.setText(description);
    }

    private void addTask() {
        String taskName = taskNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "任务名称不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Task task = new Task(taskName, description);

        if (taskDAO.addTask(task)) {
            loadTasks();
            clearFields();
            JOptionPane.showMessageDialog(this, "任务添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "任务添加失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一个任务", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int taskId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 6);

        if (!"未开始".equals(status)) {
            JOptionPane.showMessageDialog(this, "只有'未开始'状态的任务才能开始", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (taskDAO.startTask(taskId)) {
            loadTasks();
            JOptionPane.showMessageDialog(this, "任务已开始", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "任务开始失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void endSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一个任务", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int taskId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 6);

        if (!"进行中".equals(status)) {
            JOptionPane.showMessageDialog(this, "只有'进行中'状态的任务才能结束", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (taskDAO.endTask(taskId)) {
            loadTasks();
            JOptionPane.showMessageDialog(this, "任务已完成", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "任务结束失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一个任务", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String taskName = taskNameField.getText().trim();
        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "任务名称不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int taskId = (int) tableModel.getValueAt(selectedRow, 0);
        String description = descriptionArea.getText().trim();

        Task task = new Task(taskName, description);
        task.setId(taskId);

        if (taskDAO.updateTask(task)) {
            loadTasks();
            JOptionPane.showMessageDialog(this, "任务更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "任务更新失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一个任务", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int taskId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除这个任务吗?", "确认删除", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (taskDAO.deleteTask(taskId)) {
                loadTasks();
                clearFields();
                JOptionPane.showMessageDialog(this, "任务删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "任务删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        taskNameField.setText("");
        descriptionArea.setText("");
    }
}