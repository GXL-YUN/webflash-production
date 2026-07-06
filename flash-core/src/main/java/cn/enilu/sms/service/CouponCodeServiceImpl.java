package cn.enilu.sms.service;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.entity.cms.Banner;
import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.project.bean.model.ProjectModel;
import cn.enilu.project.dao.ProjectDao;
import cn.enilu.sms.bean.CouponCode;
import cn.enilu.sms.dao.CouponCodeDao;
import cn.enilu.sms.util.VerifyCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


/**
 * 核销码
 */
@Service
@Slf4j
public class CouponCodeServiceImpl  extends BaseService<CouponCode, String, CouponCodeDao> {
    @Autowired
    private ProjectDao projectDao;



    @Autowired
    private  VerifyCodeGenerator verifyCodeGenerator;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     *  生成核销码（防重）
     * @param bizType
     * @param userId
     * @param expireTime
     * @return
     */
    @Transactional
    public String createVerifyCode(String bizType, String userId, Date expireTime) {
        while (true) {
            String code = verifyCodeGenerator.generate(bizType);
            CouponCode entity = new CouponCode();
            entity.setVerifyCode(code);
            entity.setBizType(bizType);
            entity.setUserId("userId");
            entity.setStatus(0);
            entity.setExpireTime(expireTime);
            try {
                this.insert(entity);
                Long success = redisTemplate.opsForSet()
                        .add("verify:code:set:"+bizType, code);
                log.info("核销码{}",code);
                if (Boolean.TRUE.equals(success)) {
                    return code;
                }else{
                    return "";
                }
            } catch (DuplicateKeyException e) {
                // 唯一索引冲突，重试
            }
        }
    }

    /**
     * 进行核销
     */
    @Transactional
    public void verify(String verifyCode, Long userId) throws BizException {
        SearchFilter filter = null;
        if (StringUtil.isNotEmpty(verifyCode)) {
            filter = SearchFilter.build("verifyCode", SearchFilter.Operator.LIKE, verifyCode);
        }
        List<CouponCode> list = this.queryAll(filter);
        CouponCode coupon = list.get(0);

        if (coupon == null) {
            throw new BizException("核销码不存在");
        }
        if (coupon.getStatus() == 1) {
            throw new BizException("核销码已使用");
        }
//        if (coupon.getStatus() == 2 ||
//                coupon.getExpireTime().isBefore(Date.UTC())) {
//            throw new BizException("核销码已过期");
//        }

        if (!Objects.equals(coupon.getUserId(), userId)) {
            throw new BizException("无权使用该核销码");
        }

        coupon.setStatus(1);
        coupon.setUsedTime(new Date(11111111111L));
        this.update(coupon);
    }



    public String setRedisDate(){
        String code = VerifyCodeGenerator.generate("1");
        Long success = redisTemplate.opsForSet()
                .add("verify:code:set", code);
        if (Boolean.TRUE.equals(success)) {
            return code;
        }else{
            return "";
        }
    }

}
