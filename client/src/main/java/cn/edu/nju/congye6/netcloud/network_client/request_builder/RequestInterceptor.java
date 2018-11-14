package cn.edu.nju.congye6.netcloud.network_client.request_builder;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * context.getBeansOfType(RequestInterceptor.class)可以查找到所有子类
 * 子类必须是bean，即已经注册到spring
 * context.getBeansWithAnnotation
 * Created by cong on 2018-11-14.
 */
public interface RequestInterceptor {



}
