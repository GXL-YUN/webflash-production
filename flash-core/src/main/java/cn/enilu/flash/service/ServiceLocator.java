package cn.enilu.flash.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServiceLocator {

    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static <T> T getService(Class<T> serviceClass) {
        return applicationContext.getBean(serviceClass);
    }
}


//    public void someMethod() {
//        //PetService petService = ServiceLocator.getService(PetService.class);
//        // 使用 service
//    }
