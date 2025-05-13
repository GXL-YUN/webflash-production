package cn.gui.app.management.dao;

import cn.enilu.flash.api.utils.StringUtil;
import cn.gui.app.management.bean.Task;
import cn.gui.app.management.util.DBUtil;

import java.io.BufferedInputStream;
import java.nio.Buffer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDAO {
    public boolean addTask(Task task) {
        String sql = "INSERT INTO tasks(task_name, description, status) VALUES(?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        task.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean startTask(int taskId) {
        String sql = "UPDATE tasks SET start_time = NOW(), status = 'IN_PROGRESS' WHERE id = ? AND status = 'NOT_STARTED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean endTask(int taskId) {
        String sql = "UPDATE tasks SET end_time = NOW(), status = 'COMPLETED', " +
                "total_minutes = TIMESTAMPDIFF(MINUTE, start_time, NOW()) " +
                "WHERE id = ? AND status = 'IN_PROGRESS'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY create_time DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTaskName(rs.getString("task_name"));
                task.setDescription(rs.getString("description"));

                Timestamp startTime = rs.getTimestamp("start_time");
                task.setStartTime(startTime != null ? startTime.toLocalDateTime() : null);

                Timestamp endTime = rs.getTimestamp("end_time");
                task.setEndTime(endTime != null ? endTime.toLocalDateTime() : null);

                task.setTotalMinutes(rs.getInt("total_minutes"));
                task.setStatus(rs.getString("status"));

                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     *
     * 获取今日日报
     * @return
     */

    public List<Task> getNewDate() {




        List<Task> tasks = new ArrayList<>();
        StringBuilder hql = new StringBuilder("select   *  from tasks   where  end_time>(select curdate())  or update_time>(select curdate())");
        hql.append(" ORDER BY create_time DESC");

        String sql = hql.toString();

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTaskName(rs.getString("task_name"));
                task.setDescription(rs.getString("description"));

                Timestamp startTime = rs.getTimestamp("start_time");
                task.setStartTime(startTime != null ? startTime.toLocalDateTime() : null);

                Timestamp endTime = rs.getTimestamp("end_time");
                task.setEndTime(endTime != null ? endTime.toLocalDateTime() : null);

                task.setTotalMinutes(rs.getInt("total_minutes"));
                task.setStatus(rs.getString("status"));

                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public List<Task> getByName(String name,String state) {
        List<Task> tasks = new ArrayList<>();
        StringBuilder hql = new StringBuilder("SELECT * FROM tasks where  1=1");
        if(StringUtil.isNotNull(name) ){
            hql.append(" and task_name= '"+name+"'");
        }

        if(StringUtil.isNotNull(state) ){

            Map<String,String> map=new HashMap<>();
            map.put("未开始","NOT_STARTED");
            map.put("已完成","COMPLETED");
            map.put("进行中","IN_PROGRESS");
            map.put("任务暂停","STOPETED");

            if(!"全部".equals(state)){

                hql.append(" and status= '"+map.get(state)+"'");
            }

        }

        hql.append(" ORDER BY create_time DESC");
        String sql = hql.toString();

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTaskName(rs.getString("task_name"));
                task.setDescription(rs.getString("description"));

                Timestamp startTime = rs.getTimestamp("start_time");
                task.setStartTime(startTime != null ? startTime.toLocalDateTime() : null);

                Timestamp endTime = rs.getTimestamp("end_time");
                task.setEndTime(endTime != null ? endTime.toLocalDateTime() : null);

                task.setTotalMinutes(rs.getInt("total_minutes"));
                task.setStatus(rs.getString("status"));

                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET task_name = ?, description = ?,status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(4, task.getId());
            pstmt.setString(3, task.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}