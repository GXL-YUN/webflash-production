package com.sxfusion.demo.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.sxfusion.demo.function.Testname;

/**
 * 定时任务  数据备份
 * @author Lenovo
 *
 */
public class MysqlUtil {
	private static Logger logger = Logger.getLogger(Testname.class);
	/**
     * @param hostIP ip地址，可以是本机也可以是远程
     * @param userName 数据库的用户名
     * @param password 数据库的密码
     * @param savePath 备份的路径
     * @param fileName 备份的文件名
     * @param databaseName 需要备份的数据库的名称
     * @return
     */
    public static boolean backup(String hostIP, String userName, String password, String savePath, String fileName,
            String databaseName) {
        fileName +=".sql"; 
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }
        logger.info("备份执行等待");
        //拼接命令行的命令
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mysqldump").append(" --opt").append(" -h").append(hostIP);
        stringBuilder.append(" --user=").append(userName).append(" --password=").append(password)
                .append(" --lock-all-tables=true");
        stringBuilder.append(" --result-file=").append(savePath + fileName).append(" --default-character-set=utf8 ")
                .append(databaseName);
        try {
            //调用外部执行exe文件的javaAPI
            Process process = Runtime.getRuntime().exec(stringBuilder.toString());
            if (process.waitFor() == 0) {// 0 表示线程正常终止。
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * @param filepath 数据库备份的脚本路径
     * @param ip IP地址
     * @param database 数据库名称
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static boolean recover(String filepath,String ip,String database, String userName,String password) {
    

        String stmt1 = "mysqladmin -h "+ip+" -u "+userName+" -p"+password+" create "+database;

        String stmt2 = "mysql -h "+ip+" -u "+userName+" -p "+password+" "+database+" < " + filepath;

        String[] cmd = { "cmd", "/c", stmt2 };

        try {
            Runtime.getRuntime().exec(stmt1);
            Runtime.getRuntime().exec(cmd);
            System.out.println("数据已从 " + filepath + " 导入到数据库中");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
