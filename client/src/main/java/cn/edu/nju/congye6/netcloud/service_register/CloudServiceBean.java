package cn.edu.nju.congye6.netcloud.service_register;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

/**
 * Created by cong on 2018-10-22.
 */
public class CloudServiceBean implements FactoryBean<Object>{

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

    @Nullable
    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
