package cn.edu.nju.congye6.netcloud.service_register;

import cn.edu.nju.congye6.netcloud.proxy.UserCloudServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

import java.lang.reflect.Proxy;

/**
 * Created by cong on 2018-10-22.
 */
public class CloudServiceBean implements FactoryBean<Object>{

    private Class<?> type;

    /**
     * 服务名称
     */
    private String serviceName;

    @Nullable
    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(type.getClassLoader(),new Class[]{type},new CloudServiceHandler());
    }

    /**
     * 获取对象的类型
     * @return
     */
    @Nullable
    @Override
    public Class<?> getObjectType() {
        return type;
    }



    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
