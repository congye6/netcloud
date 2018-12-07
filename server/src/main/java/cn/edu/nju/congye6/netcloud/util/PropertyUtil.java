package cn.edu.nju.congye6.netcloud.util;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 获取配置文件属性
 */
public class PropertyUtil {

    public static String getProperty(String name){
        ApplicationContext context= CloudContextUtil.getContext();
        Environment environment=context.getEnvironment();
        return environment.getProperty(name);
    }

}
