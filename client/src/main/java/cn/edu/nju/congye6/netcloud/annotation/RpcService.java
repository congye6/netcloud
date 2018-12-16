package cn.edu.nju.congye6.netcloud.annotation;

import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcCallBack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cong on 2018-11-07.
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.METHOD})//定义注解的作用目标**作用范围字段、枚举的常量/方法
public @interface RpcService {

    /**
     * 调用的方法id，类似于url
     * @return
     */
    String rpcId();

    /**
     * 请求参数的类型
     * json参数只能传一个参数，将会被解析成json字符串
     * @return
     */
    RpcContentType contentType() default RpcContentType.JSON;

    /**
     * 参数中是否存在回调函数
     * 回调函数必须为最后一个参数
     * @return
     */
    boolean hasCallBack() default false;

    /**
     * 异常重试次数
     * 超时重试由其他参数设置
     * @return
     */
    int retryTimes() default 0;
}
