package cn.edu.nju.congye6.netcloud.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注册提供的服务
 * Created by cong on 2018-12-05.
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.METHOD})//定义注解的作用目标**作用范围字段、枚举的常量/方法
public @interface RpcCloudService {
    /**
     * 调用的方法id，类似于url
     * @return
     */
    String rpcId();
}
