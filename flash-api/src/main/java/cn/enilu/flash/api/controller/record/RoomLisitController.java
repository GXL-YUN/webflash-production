package cn.enilu.flash.api.controller.record;


import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.kmss.bean.entity.AnnouncementBean;
import cn.enilu.kmss.bean.entity.RoomList;
import cn.enilu.kmss.service.AnnouncementService;
import cn.enilu.kmss.service.RoomListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomLisitController {

    @Autowired
    private RoomListService roomListService;


    @Autowired
    private AnnouncementService announcementService;
    /**
     * 房屋查询
     */
    @PostMapping("/list")
    public Object list(@RequestParam(required = false) String values) {
        Page<RoomList> page = new PageFactory<RoomList>().defaultPage();
        page.addFilter("fdRoomName", SearchFilter.Operator.LIKE, values, SearchFilter.Join.or);
        page.addFilter( "fdName", SearchFilter.Operator.LIKE, values , SearchFilter.Join.or);
        page = roomListService.queryPage(page);
       //roomListService.queryIndexNews()
        List<RoomList> list = page.getRecords();
        return Rets.success(list);
    }
    /**
     * 查询房屋详情
     */
    @GetMapping("/view")
    @BussinessLog(value = "删除档案", key = "id")
    public Object showView(Long id) {

        RoomList room=roomListService.get(id);
        return Rets.success(room);
    }

    /**
     * 删除房屋
     */
    @GetMapping("/delect")
    @BussinessLog(value = "删除档案", key = "id")
    public Object remove(Long id) {
        roomListService.delete(id);
        return Rets.success();
    }
    /**
     * 新增房屋数据
     * @param recordDao
     * @return
     */
    @PostMapping("/add")
    @BussinessLog(value = "房屋新增", key = "name")
    public Object Add(@RequestBody @Valid RoomList recordDao) {
        if (recordDao.getId() == null) {

            //新增房源
           RoomList id= roomListService.insert(recordDao);
            //发布房源信息
            AnnouncementBean announcementBean=new AnnouncementBean();
            Calendar calendar = Calendar.getInstance();
            java.util.Date now = new java.util.Date();
            // 将 java.util.Date 转换为 Timestamp
            Timestamp timestamp = new Timestamp(now.getTime());
            // recordDao.setCreateTime(timestamp);
            long endTime=timestamp.getTime()+60*1000*60*24;
            announcementBean.setFdEndDate(new Timestamp(endTime));//当前时间加三天
            announcementBean.setFdName(id.getFdRoomName());
            announcementBean.setFdRoomId(id.getId().toString());
            announcementBean.setFdMassage("房源名称："+id.getFdRoomName()+"\n房源地址："+id.getFdAddres()+"\n房源备注:"+id.getFdbz());

            announcementService.insert(announcementBean);
        } else {
            roomListService.update(recordDao);
        }




        return Rets.success();
    }
}
