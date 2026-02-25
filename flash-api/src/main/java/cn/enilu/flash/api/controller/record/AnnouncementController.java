package cn.enilu.flash.api.controller.record;

import cn.enilu.flash.api.config.date.ConnectionMkOracleWrapper;
import cn.enilu.flash.api.controller.TrainBean;
import cn.enilu.flash.api.utils.rabbitMq.RabbitMQUtils;
import cn.enilu.flash.api.utils.sql.NamedParameterSqlExecutor;
import cn.enilu.flash.api.utils.sql.util.FieldUtils;
import cn.enilu.flash.api.utils.sql.util.SqlGenerator;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room/announcement")
public class AnnouncementController {


    @Autowired
    private AnnouncementService announcementService;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
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

    @Autowired
    private RabbitMQUtils rabbitMQUtils;
    @PostMapping("/list")
    public Object showView() {
        //根据时间查出来当前三天的公告
        Page<AnnouncementBean> page = new PageFactory<AnnouncementBean>().defaultPage();
        page.addFilter("fdEndDate", SearchFilter.Operator.GTE, cn.enilu.util.DateUtil.getDateQueue(), SearchFilter.Join.and);
        page = announcementService.queryPage(page);
        //roomListService.queryIndexNews();
        List<AnnouncementBean> list = page.getRecords();

        return Rets.success(list);
    }
    @Autowired
    private ConnectionMkOracleWrapper connectionMkOracleWrapper;
    @Resource
    private NamedParameterSqlExecutor sqlExecutor;
    @PostMapping("/test")
    public Object test() {
        TrainBean trainBean = new TrainBean();


        trainBean.setFdId("233223"); //培训人员账号  fd_col_ivgm2j
        trainBean.setFdNum("32324"); //课程名称  fd_col_9tjgjh

        String sql = SqlGenerator.generateInsertSql(trainBean);
        Map<String, Object> paramMap= FieldUtils.getNotNullFieldsWithAlias(trainBean, false);
        try (Connection ekpConn = connectionMkOracleWrapper.getConnection()) {
            sqlExecutor.executeInsertAndReturnKey(sql, paramMap, "fd_id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Rets.success("");
    }

    public static void main(String[] args) {
        TrainBean trainBean = new TrainBean();

        trainBean.setFdId("233223"); //培训人员账号  fd_col_ivgm2j
        trainBean.setFdNum("32324"); //课程名称  fd_col_9tjgjh

        String sql = SqlGenerator.generateInsertSql(trainBean);
        Map<String, Object> paramMap= FieldUtils.getNotNullFieldsWithAlias(trainBean);

    }
}


