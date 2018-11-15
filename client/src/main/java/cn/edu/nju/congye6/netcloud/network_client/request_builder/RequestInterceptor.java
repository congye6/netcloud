package cn.edu.nju.congye6.netcloud.network_client.request_builder;

/**
 * context.getBeansOfType(RequestInterceptor.class)可以查找到所有子类
 * 子类必须是bean，即已经注册到spring
 * context.getBeansWithAnnotation
 * Created by cong on 2018-11-14.
 */
public interface RequestInterceptor {

    /**
     * 由用户实现，cloudservice调用来修改请求
     * @param requestBuilder
     */
    public void apply(RequestBuilder requestBuilder);

}
