package cn.enilu.flash.sys.att.service;

import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.dao.BaseRepository;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public  interface SysAttMainServiceImp {


    /**
     * 批量生成附件
     */
    int addSysAttMain(List<FileInfo> fileInfoList, String  className,String  fdId);

}
