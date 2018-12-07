package cn.edu.nju.congye6.netcloud.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring上下文管理
 * Created by cong on 2018-11-15.
 */
public class CloudContextUtil implements ApplicationContextAware{

    /**
     * spring上下文
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        CloudContextUtil.applicationContext=applicationContext;
    }

    public static ApplicationContext getContext(){
        return applicationContext;
    }
}
