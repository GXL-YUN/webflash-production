package cn.enilu.flash.api.controller.tencent;
import cn.enilu.flash.api.controller.CrudController;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.Listener.SysMessage;
import cn.enilu.pet.bean.model.Orders;
import cn.enilu.pet.dao.DemandDao;
import cn.enilu.pet.dao.OrdersDao;
import cn.enilu.pet.server.OrdersServiceImpl;
import cn.enilu.sms.service.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping("/api/tencent")
public class TencentController {

    /**
     * 创建订单  存在幂等
     */
    @GetMapping("/callback")
    public Object create(@Valid @RequestParam String  code,@Valid @RequestParam String  state)  {
        log.info("回调开始code {} state {}",code, state);
        return Rets.failure("回调成功");

    }

}

