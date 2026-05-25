package cn.enilu.flash.api.controller.pet;

import cn.enilu.flash.api.controller.CrudController;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.pet.bean.model.Demand;
import cn.enilu.pet.bean.model.Quotes;
import cn.enilu.pet.dao.DemandDao;
import cn.enilu.pet.em.DemandStatus;
import cn.enilu.pet.em.QuotesStstus;
import cn.enilu.pet.server.DemandServiceImpl;
import cn.enilu.sms.service.CouponCodeServiceImpl;
import cn.enilu.util.email.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pet/demand")
public class DemandController extends
        CrudController<Demand, String, DemandDao, RequertInfo> {


    @Autowired
    private DemandServiceImpl demandServiceImpl;


    @Override
    protected BaseService<Demand, String, DemandDao>  getService() {
        return demandServiceImpl;
    }



    @Autowired
    private CouponCodeServiceImpl couponCodeServiceImpl;


    /**
     * 查询列表
     * @param user_id
     * @param status
     * @return
     */
    @GetMapping("/geList")
    public Object geList(@RequestParam String user_id,@RequestParam(required = false) String status) {

        try {
            Page<Demand> page = new PageFactory<Demand>().defaultPage();
            page.addFilter("fdStatus", SearchFilter.Operator.EQ, status, SearchFilter.Join.and);
            page.addFilter("fdUserId", SearchFilter.Operator.EQ, user_id, SearchFilter.Join.and);

            page.addFilter("fdDownStatus", SearchFilter.Operator.EQ, DemandStatus.PUBLISHED.getCode(), SearchFilter.Join.and);
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            page.setSort(sort);
            Page<Demand> date= getService().queryPage(page);
            MailUtils emin=new MailUtils();
            /**
             * 邮件
             */
            emin.sendMail("13720661531@163.com","测试","cehsi hsu ",false);
            couponCodeServiceImpl.createVerifyCode("21312312","2131",new Date(123131231L));
            return Rets.success(date.getRecords());
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }
    /**
     * 下架需求
     * 需求下架  报价完成  收货  发货  不允许报价
     */
    @PutMapping("/down/{id}")
    public Object down(@PathVariable String id, @RequestBody(required = false) String remark) {
        // 1. 获取需求记录

        try{
            Demand demand = demandServiceImpl.getById(id);
            if (demand == null) {
                return Rets.failure("需求不存在");
            }
            // 2. 状态校验  是否已经下架
            if(DemandStatus.getByCode(demand.getFdStatus())==DemandStatus.OFFLINE){
                return Rets.failure("需求已下架");
            }else if(DemandStatus.getByCode(demand.getFdStatus())==DemandStatus.PUBLISHED) {
                return Rets.failure("需求已报价完成");
            }else if(DemandStatus.getByCode(demand.getFdStatus())==DemandStatus.TAKEDELIVERYOFGOODS) {
                return Rets.failure("需求已收货");
            }else if(DemandStatus.getByCode(demand.getFdStatus())==DemandStatus.SHIPMENTS) {
                return Rets.failure("需求已发货");
            }else{
                // 3. 更新状态  置为下架状态
                demand.setFdDownStatus(DemandStatus.OFFLINE.getCode());
                // 4. 执行更新
                //查询对应数据报价记录
                List<Quotes> fdQuotesList = demand.getFdQuotesList();
                if(fdQuotesList.size()>0){
                    for(Quotes quotes:fdQuotesList){
                        quotes.setFdStatus(QuotesStstus.CANAEL.getCode());//已取消
                    }
                }
                demandServiceImpl.update(demand);
                return Rets.success("下架成功");
            }
        }catch (Exception e){
            return Rets.failure("下架失败");

        }
    }


    /**
     * 用户接受报价
     * 只有报价中的单据才能进行确认报价
     */
    @PutMapping("/affirmDate/{id}")
    public Object affirmDate(@RequestParam String user_id,@RequestParam String fd_quotes_id,@RequestParam String fd_demand_id) {
        // 1. 获取需求记录
        try{
            Demand demand = demandServiceImpl.getById(fd_demand_id);
            if (demand == null) {
                return Rets.failure("需求不存在");
            }
            // 2. 状态校验  是否"报价中"
            if(DemandStatus.getByCode(demand.getFdStatus())==DemandStatus.COMPLETED){

                //查询报价记录
                Quotes quotes=new Quotes();
                quotes.getFdDemand().setFdStatus(DemandStatus.PUBLISHED.getCode());//需求单置为”确认报价“
                // 3. 更新状态  置为下架状态
                demand.setFdDownStatus(DemandStatus.OFFLINE.getCode());//将该报价置为"中价"
                // 4. 执行更新
                //查询对应数据报价记录
                List<Quotes> fdQuotesList = demand.getFdQuotesList();
                if(fdQuotesList.size()>0){
                    for(Quotes date:fdQuotesList){
                        date.setFdStatus(QuotesStstus.REJECTED.getCode());//已取消
                    }
                }
                demandServiceImpl.update(demand);
                return Rets.success("报价成功");
            }else{
                return Rets.failure("确认报价失败，状态不正确");
            }
        }catch (Exception e){
            return Rets.failure("下架失败");
        }
    }



    /**
     * 用户取消报价
     * 只有报价中的单据才能进行确认报价
     */
    @PutMapping("/CenCelDate/{id}")
    public Object CenCelDate(@RequestParam String user_id,@RequestParam String fd_quotes_id,@RequestParam String fd_demand_id) {
        // 1. 获取需求记录
        try{
            Demand demand = demandServiceImpl.getById(fd_demand_id);
            if (demand == null) {
                return Rets.failure("需求不存在");
            }
            // 2. 状态校验  是否"报价中"
            if(DemandStatus.getByCode(demand.getFdStatus())==DemandStatus.AFFIRMDATE){

                //查询报价记录
                List<Quotes> quotes=demand.getFdQuotesList();
                // 3. 更新状态  置为”报价中“
                demand.setFdDownStatus(DemandStatus.COMPLETED.getCode());//将该报价置为"中价"
                //查询对应数据报价记录
                List<Quotes> fdQuotesList = demand.getFdQuotesList();
                if(fdQuotesList.size()>0){
                    for(Quotes date:fdQuotesList){
                        date.setFdStatus(QuotesStstus.INTHEQUOTATION.getCode());//已取消
                    }
                }
                demandServiceImpl.update(demand);
                return Rets.success("取消订单成功");
            }else{
                return Rets.failure("取消订单失败，状态不正确");
            }


        }catch (Exception e){
            return Rets.failure("取消订单失败报价失败");

        }
    }




    /**
     * 平台热门需求
     */

    @GetMapping("/geHostList")
    public Object geHostList(@RequestParam(required = false) String user_id,@RequestParam(required = false) String min_quotes) {
        try {
            Page<Demand> page = new PageFactory<Demand>().defaultPage();
            //page.addFilter("fdStatus", SearchFilter.Operator.EQ, status, SearchFilter.Join.and);
            //page.addFilter("fdUserId", SearchFilter.Operator.EQ, user_id, SearchFilter.Join.and);
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            page.setSort(sort);
            Page<Demand> date= getService().queryPage(page);
            return Rets.success(date.getRecords());
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }
}
