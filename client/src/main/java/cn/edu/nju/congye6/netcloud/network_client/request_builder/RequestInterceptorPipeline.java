package cn.edu.nju.congye6.netcloud.network_client.request_builder;

import cn.edu.nju.congye6.netcloud.util.CloudContextUtil;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * Created by cong on 2018-11-15.
 */
public class RequestInterceptorPipeline {

    /**
     * 找出所有interceptor，并执行
     * @param requestBuilder
     */
    public void pipeline(RequestBuilder requestBuilder){
        ApplicationContext context= CloudContextUtil.getContext();
        //根据接口查找bean
        Collection<RequestInterceptor> interceptors=context.getBeansOfType(RequestInterceptor.class).values();
        for(RequestInterceptor interceptor:interceptors){
            interceptor.apply(requestBuilder);
        }
    }

}
