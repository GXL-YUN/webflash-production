package cn.enilu.flash.api.controller.record;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.kmss.bean.entity.AnnouncementBean;
import cn.enilu.kmss.bean.entity.RoomList;
import cn.enilu.kmss.service.AnnouncementService;
import cn.enilu.kmss.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/room/announcement")
public class AnnouncementController {


    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/add")
    public Object Add(@RequestBody @Valid AnnouncementBean recordDao) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = new java.util.Date();
        // 将 java.util.Date 转换为 Timestamp
        Timestamp timestamp = new Timestamp(now.getTime());
       // recordDao.setCreateTime(timestamp);
        long endTime=timestamp.getTime()+60*1000*60*24*3;
        recordDao.setFdEndDate(new Timestamp(endTime));//当前时间加三天
        announcementService.insert(recordDao);

        return Rets.success(null);
    }
    @PostMapping("/list")
    public Object showView() {
        //根据时间查出来当前三天的公告

        //





        Page<AnnouncementBean> page = new PageFactory<AnnouncementBean>().defaultPage();
        page.addFilter("fdEndDate", SearchFilter.Operator.GTE, cn.enilu.util.DateUtil.getDateQueue(), SearchFilter.Join.and);
        page = announcementService.queryPage(page);
        //roomListService.queryIndexNews();
        List<AnnouncementBean> list = page.getRecords();
        return Rets.success(list);
    }
}
