package cn.gui.app.management.main;

import cn.enilu.flash.api.utils.StringUtil;
import cn.enilu.flash.utils.DateUtil;
import cn.gui.app.management.bean.Task;
import cn.gui.app.management.dao.TaskDAO;
import cn.gui.app.management.util.BasicAuthHttpClientJackson;
import cn.gui.app.management.util.HttpClientUtil;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.*;

public class MainApplicationFrame extends JFrame {

    private TaskDAO taskDAO;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField taskNameField;
    private JTextArea descriptionArea;
    private  JComboBox<String> comboBoxs;
    private JTextArea descriptionAreaAdd;
    private JComboBox<String> comboBox;
    public MainApplicationFrame() {
        taskDAO = new TaskDAO();
        // 设置窗口标题和大小
        setTitle("项目管理系统");
        setSize(1000, 800);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 主容器使用 BorderLayout
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // 1. 左侧项目目录面板 (使用JTree)
        JPanel leftPanel = createLeftDirectoryPanel();
        container.add(leftPanel, BorderLayout.WEST);

        // 2. 顶部操作栏面板
        JPanel topPanel = createTopOperationPanel();
        container.add(topPanel, BorderLayout.NORTH);

        // 3. 中部内容面板
        JPanel centerPanel = createCenterContentPanel();
        container.add(centerPanel, BorderLayout.CENTER);

        // 4. 右侧按钮面板
        JPanel rightPanel = createRightButtonPanel();
        container.add(rightPanel, BorderLayout.EAST);

        // 5. 底部表格面板
        JScrollPane bottomPanel = createBottomTablePanel();

        container.add(bottomPanel, BorderLayout.SOUTH);
        loadTasks();
    }

    // 创建左侧目录面板（保持不变）
    private JPanel createLeftDirectoryPanel() {
        getProject();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(BorderFactory.createTitledBorder("项目目录"));

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("项目根目录");
        DefaultMutableTreeNode srcNode = new DefaultMutableTreeNode("src");
        DefaultMutableTreeNode resNode = new DefaultMutableTreeNode("resources");
        DefaultMutableTreeNode docNode = new DefaultMutableTreeNode("documents");

        root.add(srcNode);
        root.add(resNode);
        root.add(docNode);

        srcNode.add(new DefaultMutableTreeNode("main"));
        srcNode.add(new DefaultMutableTreeNode("test"));
        resNode.add(new DefaultMutableTreeNode("images"));
        resNode.add(new DefaultMutableTreeNode("config"));

        JTree tree = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(tree);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // 创建顶部操作栏面板（保持不变）
    private JPanel createTopOperationPanel() {
        // 任务详情面板
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createTitledBorder("任务详情"));

        detailPanel.setPreferredSize(new Dimension(100, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailPanel.add(new JLabel("任务名称:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        taskNameField = new JTextField(80);
        detailPanel.add(taskNameField, gbc);

        String[] item = {"项目","项目", "仙姑", "项目","项目"};
        comboBoxs = new JComboBox<>(item);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        detailPanel.add(comboBoxs, gbc);




        String[] items = {"全部","已完成", "未开始", "进行中","任务暂停"};
        comboBox = new JComboBox<>(items);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        detailPanel.add(comboBox, gbc);




        JButton collectStop = new JButton("暂停");
        collectStop.addActionListener(e -> updateSelectedTaskStop("STOPETED","暂停任务"));
        gbc.gridx = 5;
        detailPanel.add(collectStop, gbc);



        JButton collectStopChange = new JButton("开启");
        collectStopChange.addActionListener(e -> updateSelectedTaskStop("IN_PROGRESS","任务从新开启"));
        gbc.gridx = 7;
        detailPanel.add(collectStopChange, gbc);

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
        collect.addActionListener(e -> {
            try {
                getNewDate();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        gbc.gridx = 5;
        detailPanel.add(collect, gbc);
        //追加数据
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        detailPanel.add(new JLabel("内容追加:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        descriptionAreaAdd = new JTextArea(3, 60);
        descriptionAreaAdd.setLineWrap(true);
        detailPanel.add(new JScrollPane(descriptionAreaAdd), gbc);

/*

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("操作栏"));
        panel.setPreferredSize(new Dimension(100, 50));
        JButton newBtn = new JButton("新建");
        JButton openBtn = new JButton("打开");
        JButton saveBtn = new JButton("保存");
        JButton deleteBtn = new JButton("删除");
        JButton refreshBtn = new JButton("刷新");
*/

/*        panel.add(newBtn);
        panel.add(openBtn);
        panel.add(saveBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);*/
       // panel.add(detailPanel);

        return detailPanel;
    }

    // 创建中部内容面板（保持不变）
    private JPanel createCenterContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("内容区"));

        JTextArea contentArea = new JTextArea();
        contentArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(contentArea);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // 创建右侧按钮面板（保持不变）
    private JPanel createRightButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("操作按钮"));
        panel.setPreferredSize(new Dimension(150, 0));

        JButton btn1 = new JButton("属性");
        JButton btn2 = new JButton("设置");
        JButton btn3 = new JButton("导出");
        JButton btn4 = new JButton("导入");
        JButton btn5 = new JButton("帮助");

        btn1.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn2.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn3.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn4.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn5.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension btnSize = new Dimension(120, 30);
        btn1.setPreferredSize(btnSize);
        btn2.setPreferredSize(btnSize);
        btn3.setPreferredSize(btnSize);
        btn4.setPreferredSize(btnSize);
        btn5.setPreferredSize(btnSize);

        panel.add(Box.createVerticalStrut(10));
        panel.add(btn1);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn2);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn3);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn4);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn5);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // 新增：创建底部表格面板
    private JScrollPane createBottomTablePanel() {
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
                            c.setBackground(new Color(220, 220, 220)); // 浅灰色
                            break;
                        case "已完成":
                            c.setBackground(new Color(200, 255, 200)); // 浅绿色
                            break;
                        case "任务暂停":
                            c.setBackground(new Color(255, 0, 0)); // 红色
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
       return tableScrollPane;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.setVisible(true);
        });
    }



    /**
     * 任务暂停
     */
    private void updateSelectedTaskStop(String start,String massage) {{
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
        String description="";
        if(StringUtil.isNotNull(descriptionAreaAdd.getText().trim())){
            description = descriptionArea.getText().trim()+"\n"+ DateUtil.getTime(new Date())+":"+descriptionAreaAdd.getText().trim();
        }else{
            description = descriptionArea.getText().trim();
        }

        Task task = new Task(taskName, description,false);
        task.setId(taskId);
        task.setStatus(start);
        if (taskDAO.updateTask(task)) {
            clearFields();
            loadTasks();
            JOptionPane.showMessageDialog(this, massage+"成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, massage+"失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    }

    /**
     * 获取今日日报
     */
    private void getNewDate() throws Exception {
        java.util.List<Task> tasks = taskDAO.getNewDate();
        StringBuilder date=new StringBuilder("");
        Map<String,String> map=new HashMap<>();
        map.put("NOT_STARTED","未开始");
        map.put("COMPLETED","已完成");
        map.put("IN_PROGRESS","进行中");
        map.put("STOPETED","任务暂停");
        for (Task task:tasks){
            date.append("任务名称："+task.getTaskName()+" 任务描述："+task.getDescription().replaceAll("\\s*|\t|\r|\n", "")+" 是否完成："+map.get(task.getStatus())+"\n");
        }

        BasicAuthHttpClientJackson.create(tasks);
        descriptionArea.setText(date.toString());
        JOptionPane.showMessageDialog(this, "任务发送成功", "成功", JOptionPane.YES_OPTION);
        return;
    }


    private void loadTasks() {
        java.util.List<Task> tasks = taskDAO.getAllTasks();
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
            case "STOPETED": return "任务暂停";
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


        //获取项目类别

        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "任务名称不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Task task = new Task(taskName, description,true);

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
//            return;
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
        String description="";
        if(StringUtil.isNotNull(descriptionAreaAdd.getText().trim())){
            description = descriptionArea.getText().trim()+"\n"+ DateUtil.formatDate(new Date(),"yyyy-MM-dd")+":"+descriptionAreaAdd.getText().trim();
        }else{
            description = descriptionArea.getText().trim();
        }

        Task task = new Task(taskName, description,false);
        task.setId(taskId);

        if (taskDAO.updateTask(task)) {
            clearFields();
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
        descriptionAreaAdd.setText("");
    }

    /**
     * 拉取项目
     * 拉取项目
     */
   private void getProject(){

       JSONObject json=new JSONObject();
       JSONObject curUser=new JSONObject();
       curUser.put("fdLoginName","guxinlei");
       json.put("curUser",curUser);
       Map<String, String> map=new HashMap<>();
       map.put("Authorization","Basic Z3hsOmd4bEAwNTE5");
       map.put("Content-Type","application/json");
       System.out.println(  HttpClientUtil.postAsync("http://123.249.96.139/openapi/sys-modeling/apis/data/ls-gsgl/V1/xm/list", json.toString(), map)
               .thenAccept(response -> dateChange(response)));

    }


    /**
     * 对于数据处理
     */
    private void dateChange(String response){

        System.out.println("响应结果: " + response);
        JSONObject paramStr = new JSONObject();
        paramStr =new  JSONObject(response);
        paramStr.getJSONObject("data").getJSONObject("")js

    }

}