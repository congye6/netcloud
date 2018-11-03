package cn.edu.nju.congye6.netcloud.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by cong on 2018-11-03.
 */
@Component
/**
 * 获取配置文件属性
 * 因为需要交给spring管理才能获取context
 * 所以使用了注解
 */
public class PropertyUtil implements ApplicationContextAware{

    private static ApplicationContext context;

    public static String getProperty(String name){
        Environment environment=context.getEnvironment();
        return environment.getProperty(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }
}
