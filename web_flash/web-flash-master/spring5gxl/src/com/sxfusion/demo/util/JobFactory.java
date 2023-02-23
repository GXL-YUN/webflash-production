package com.sxfusion.demo.util;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * 解决job无法实现注入bean
 * job对象在spring容器加载时候，能够注入bean，但是调度时，job对象会重新创建，此时就是导致已经注入的对象丢失，因此报空指针异常。
 * @author gxl
 *
 */
public class JobFactory extends AdaptableJobFactory {
    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        //调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        //进行注入,这属于Spring的技术,不清楚的可以查看Spring的API.
        autowireCapableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }

}