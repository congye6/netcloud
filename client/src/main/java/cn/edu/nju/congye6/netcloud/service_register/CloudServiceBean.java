package cn.edu.nju.congye6.netcloud.service_register;

import cn.edu.nju.congye6.netcloud.proxy.UserCloudServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

/**
 * Created by cong on 2018-10-22.
 */
public class CloudServiceBean implements FactoryBean<Object>, InitializingBean,
        ApplicationContextAware{

    private Class<?> type;

    /**
     * 服务名称
     */
    private String serviceName;

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

    @Nullable
    @Override
    public Object getObject() throws Exception {
        return new UserCloudServiceImpl();
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


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("start");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
