package cn.edu.nju.congye6.netcloud.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * spring上下文管理
 * Created by cong on 2018-11-15.
 */
public class CloudContextUtil{

    /**
     * spring上下文
     */
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CloudContextUtil.applicationContext=applicationContext;
    }

    public static ApplicationContext getContext(){
        return applicationContext;
    }
}
