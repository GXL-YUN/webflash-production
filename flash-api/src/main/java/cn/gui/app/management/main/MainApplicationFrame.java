package cn.gui.app.management.main;

import cn.enilu.flash.api.utils.StringUtil;
import cn.enilu.flash.utils.DateUtil;
import cn.gui.app.management.bean.Annotation;
import cn.gui.app.management.bean.ProjectBean;
import cn.gui.app.management.bean.Task;
import cn.gui.app.management.bean.TreeNodeData;
import cn.gui.app.management.dao.AnnotationDao;
import cn.gui.app.management.dao.ProjctDao;
import cn.gui.app.management.dao.TaskDAO;
import cn.gui.app.management.util.BasicAuthHttpClientJackson;
import cn.gui.app.management.util.HttpClientUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.table.*;

public class MainApplicationFrame extends JFrame {

    private TaskDAO taskDAO;

    private AnnotationDao annotationDao;
    private JTable taskTable;
    private JTable   annotationtaskTable;
    //任务
    private DefaultTableModel tableModel;

    //批注
    private DefaultTableModel annotationtableModel;
    private JTextField taskNameField;
    private JTextArea descriptionArea;
    private  JComboBox<String> comboBoxs;
    private JTextArea descriptionAreaAdd;
    private JComboBox<String> comboBox;

    //当前所选项目id
    private String projectId;

    public MainApplicationFrame() {
        taskDAO = new TaskDAO();
        annotationDao=new AnnotationDao();
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
        JPanel bottomPanel = createBottomTablePanel();

        container.add(bottomPanel, BorderLayout.SOUTH);
        loadTasks();

        // 表格选择监听器
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displayTaskDetails(selectedRow);

                }
            }
        });
    }
    // 创建左侧目录面板（保持不变）
    private JPanel createLeftDirectoryPanel() {
        //拉取项目
       //getProject();
        ProjctDao dao=new ProjctDao();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBorder(BorderFactory.createTitledBorder("所有项目"));


        //查询项目路径
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("项目根目录");



        List<ProjectBean> list=dao.getAllTasks("1");

        for (ProjectBean projrctBean:list){

            TreeNodeData tst=new TreeNodeData(projrctBean.getFdName(),projrctBean.getFdId());
            DefaultMutableTreeNode srcNode = new DefaultMutableTreeNode(tst);
            //查询项目计划   查询一级二级三级
            root.add(srcNode);
        }
        JTree tree = new JTree(root);
        // 2. 添加选中监听器
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                // 获取选中的节点
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)
                        tree.getLastSelectedPathComponent();

                if (selectedNode != null) {
                    projectId=((TreeNodeData)selectedNode.getUserObject()).getRealValue();
                    System.out.println("选中节点: " + selectedNode.getUserObject());
                    System.out.println("选中节点: "+projectId );
                    clearFields();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tree);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // 创建顶部操作栏面板（保持不变）
    private JPanel createTopOperationPanel() {
        // 任务详情面板
        JPanel detailPanel = new JPanel(new GridBagLayout());

        detailPanel.setBorder(BorderFactory.createTitledBorder("任务详情"));
        detailPanel.setPreferredSize(new Dimension(1000, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.insets = new Insets(10, 10, 10, 10);
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2;
        gbc.gridy = 0;
        detailPanel.add(new JLabel("任务名称:"), gbc);
        gbc.gridx = 4;
       // gbc.gridwidth = 1;
        taskNameField = new JTextField(60);
        detailPanel.add(taskNameField, gbc);

        JButton select = new JButton("查询");
        select.addActionListener(e -> select());
        gbc.gridx = 6;
        detailPanel.add(select, gbc);


        //第二行
        String[] items = {"全部","已完成", "未开始", "进行中","任务暂停"};
        comboBox = new JComboBox<>(items);
        gbc.gridy = 0;
        gbc.gridx = 0;
       // gbc.gridwidth = 1;
        detailPanel.add(comboBox, gbc);
        return detailPanel;
    }

    // 创建中部内容面板（保持不变）
    private JPanel createCenterContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        //修改成针对每个任务做的回复每天

        panel.setBorder(BorderFactory.createTitledBorder("任务详情描述"));


        descriptionArea= new JTextArea(10,60);
        descriptionArea.setFont(new Font("宋体", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(operation);
    }

    // 创建右侧按钮面板（保持不变）
    private JPanel createRightButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("操作按钮"));
        panel.setPreferredSize(new Dimension(150, 0));



        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(10, 1, 5, 5));

        JButton addButton = new JButton("添加任务");
        addButton.addActionListener(e -> addTask());

        JButton startButton = new JButton("开始任务");
        startButton.addActionListener(e -> startSelectedTask());

        JButton endButton = new JButton("结束任务");
        endButton.addActionListener(e -> endSelectedTask());

        JButton updateButton = new JButton("添加批注");
        updateButton.addActionListener(e -> updateSelectedTaskStop("新增批注"));

        JButton deleteButton = new JButton("删除任务");
        deleteButton.addActionListener(e -> deleteSelectedTask());

/*
        JButton collectStop = new JButton("暂停任务");
        collectStop.addActionListener(e -> updateSelectedTaskStop("STOPETED","暂停任务"));
*/

        /*JButton collectStopChange = new JButton("开启任务");
        collectStopChange.addActionListener(e -> updateSelectedTaskStop("IN_PROGRESS","任务从新开启"));
*/
        JButton collect = new JButton("汇总任务");
        collect.addActionListener(e -> {
            try {
                getNewDate();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(startButton);
        buttonPanel.add(endButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        //buttonPanel.add(collectStop);
        //buttonPanel.add(collectStopChange);
        buttonPanel.add(collect);
        JScrollPane scrollPane = new JScrollPane(panel);
        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }
    // 新增：创建底部表格面板
    private JPanel createBottomTablePanel() {
        JPanel detailPanel = new JPanel(new GridLayout());

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
        //批注列表格
        String[]  annotationCoulm= {"批注时间", "批注内容"};
        annotationtableModel = new DefaultTableModel(annotationCoulm, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        annotationtaskTable = new JTable(annotationtableModel);
        // 设置自定义渲染器
        annotationtaskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                // 获取状态列的值（假设状态是第2列，索引从0开始）
                //String status = (String) table.getModel().getValueAt(row, 6);
                return c;
            }
        });
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        annotationtaskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        JScrollPane tableScrollPanes = new JScrollPane(annotationtaskTable);

//        tableScrollPane.setPreferredSize(new Dimension(700, 400));
//        tableScrollPanes.setPreferredSize(new Dimension(200, 400));


        tableScrollPane.setColumnHeaderView(new JLabel("任务清单"));
        tableScrollPanes.setColumnHeaderView(new JLabel("批注内容"));
        detailPanel.add(tableScrollPane);
        detailPanel.add(tableScrollPanes);
       return detailPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.setVisible(true);
        });
    }



    /**
     * 批注添加
     */
    private void updateSelectedTaskStop(String massage) {
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
        if(StringUtil.isNotNull(descriptionArea.getText().trim())){
            description = descriptionArea.getText().trim();
        }else{
            description = descriptionArea.getText().trim();
        }


        Annotation ans=new Annotation();
       // ans.setFdCreateTime();
        ans.setFdMainId(taskId+"");
        ans.setFdMassage(description);
        ans.setFdId(UUID.randomUUID().toString());
        if (annotationDao.addTask(ans)) {
            clearFields();
            loadTasks();
            JOptionPane.showMessageDialog(this, massage+"成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, massage + "失败", "错误", JOptionPane.ERROR_MESSAGE);
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
    private void loadAnnotationtableModel( int selectedRow) {

        //= annotationtaskTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一个任务", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        java.util.List<Annotation> tasks = annotationDao.getAllTasks(""+selectedRow);
        annotationtableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Annotation task : tasks) {
            Object[] rowData = {
                    task.getFdCreateTime() != null ? task.getFdCreateTime().format(formatter) : "",
                    task.getFdMassage()
            };
            annotationtableModel.addRow(rowData);
        }
    }
    private void select() {
        String taskName = taskNameField.getText().trim();

        String selected = (String) comboBox.getSelectedItem();
        List<Task> tasks = taskDAO.getByName(taskName,selected.trim(),projectId);
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

        loadAnnotationtableModel(id);
    }

    private void addTask() {
        String taskName = taskNameField.getText().trim();
        String description = descriptionArea.getText().trim();


        //获取项目类别

        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "任务名称不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(StringUtil.isNotNull(projectId)){
            Task task = new Task(taskName, description,true,projectId);
            if (taskDAO.addTask(task)) {
                loadTasks();
                clearFields();
                JOptionPane.showMessageDialog(this, "任务添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "任务添加失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "请选择项目类别", "错误", JOptionPane.ERROR_MESSAGE);
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

        if(StringUtil.isNotNull(projectId)){

            Task task = new Task(taskName, description,false,projectId);
            task.setId(taskId);

            if (taskDAO.updateTask(task)) {
                clearFields();
                loadTasks();
                JOptionPane.showMessageDialog(this, "任务更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "任务更新失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "请选择项目类别", "错误", JOptionPane.ERROR_MESSAGE);

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
       // descriptionAreaAdd.setText("");
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
       json.put("pageSize",1000);
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
        ProjctDao dao=new ProjctDao();

        JSONObject paramStr = new JSONObject();
        paramStr =new  JSONObject(response);
        JSONArray arr=paramStr.getJSONObject("data").getJSONArray("content");
        for (Object date:arr){
            JSONObject l= (JSONObject) date;
            ProjectBean bean=new ProjectBean();
            bean.setFdId(l.getString("fd_id"));
            bean.setFdName(l.getString("fd_col_sv2q1k"));

            try{
                dao.addTask(bean);
            }catch (Exception e){
                System.out.println("存在该记录: " + l.getString("fd_id"));
            }


        }

    }

}