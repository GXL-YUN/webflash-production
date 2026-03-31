package cn.enilu.flash.sys.att.service.imp;

import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.flash.sys.att.bean.SysAttMain;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.sys.att.service.SysAttMainServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SysAttMainService extends BaseService<SysAttMain, String, BaseRepository<SysAttMain, String >> implements SysAttMainServiceImp {


    /**
     * 将FileInfo转换为SysAttMain
     * @param fileInfoList 文件信息
     * @return 附件主表实体
     */

    /**
     *
     * @param fileInfoList    附件列表
     * @param className    类名
     * @param fdId   id
     * @return
     */
    @Override
    @Async  //异步
    @Transactional(rollbackFor = Exception.class)
    public int addSysAttMain(List<FileInfo> fileInfoList, String  className, String  fdId) {
        if (fileInfoList == null || fileInfoList.isEmpty()) {
            return 0;
        }
        int i=0;
        try {
            // 将FileInfo转换为SysAttMain实体
            List<SysAttMain> sysAttMainList = new ArrayList<>();
            for (FileInfo fileInfo : fileInfoList) {
                SysAttMain sysAttMain = new SysAttMain();
                sysAttMain.setFdModelName(className);//模块名称
                sysAttMain.setFdModelId(fdId);
                sysAttMain.setFdKey(fileInfo.getFdKey());
                sysAttMain.setFd_file_id(fileInfo.getFdId());
                this.insert(sysAttMain);
                i++;
            }
            log.error("批量新增附件数据成功", 1);
            return i;

        } catch (Exception e) {
            // 记录日志
            log.error("批量新增附件数据失败", e);
            // 抛出运行时异常触发事务回滚
            throw new RuntimeException("附件保存失败", e);
        }

    }
}
