package cn.gui.app.management.dao;

import cn.gui.app.management.bean.ProjectBean;
import cn.gui.app.management.bean.Task;
import cn.gui.app.management.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjctDao {

    /**
     *   新增
     */
    public boolean addTask(ProjectBean task) {
        String sql = "INSERT INTO ProjectBean(fdId, fdName) VALUES(?, ?,1)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, task.getFdId());
            pstmt.setString(2, task.getFdName());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        task.setFdId(rs.getString(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     *   更新
     */
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


    /**
     *   查询
     */

    public List<ProjectBean> getAllTasks(String type) {
        List<ProjectBean> tasks = new ArrayList<>();
        String sql = "SELECT * FROM ProjectBean  where fdType = '1' and fdIsMe='1'  ";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProjectBean rojectBean = new ProjectBean();
                rojectBean.setFdId(rs.getString("fdId"));
                rojectBean.setFdName(rs.getString("fdName"));

                tasks.add(rojectBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * 查询名称
     */

    public List<ProjectBean> getTaskName(String id) {
        List<ProjectBean> tasks = new ArrayList<>();
        String sql = "SELECT * FROM ProjectBean  where fdId = '"+id+"'";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           // pstmt.setString(1, id);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProjectBean rojectBean = new ProjectBean();
                rojectBean.setFdId(rs.getString("fdId"));
                rojectBean.setFdName(rs.getString("fdName"));

                tasks.add(rojectBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }



}
