package cn.edu.nju.congye6.netcloud.service_register;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 远程服务的代理类
 * Created by cong on 2018-10-23.
 */
public class CloudServiceHandler implements InvocationHandler{

    /**
     * 调用的服务名称
     */
    private String serviceName;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RequestMapping requestMapping=method.getAnnotation(RequestMapping.class);
        if(requestMapping==null|| StringUtils.isEmpty(requestMapping.value()))
            throw new Exception("please configure url with @RequestMapping");
        String path=requestMapping.value()[0];

        return null;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
