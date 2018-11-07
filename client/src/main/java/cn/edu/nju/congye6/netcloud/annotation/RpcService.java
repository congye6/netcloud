package cn.edu.nju.congye6.netcloud.annotation;

import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;

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

    RpcContentType contentType() default RpcContentType.JSON;

}
