package cn.edu.nju.congye6.netcloud.annotation;

import org.springframework.lang.Nullable;

import javax.lang.model.type.NullType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cong on 2018-10-19.
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.TYPE})//定义注解的作用目标**作用范围字段、枚举的常量/方法
public @interface CloudService {


    /**
     * 微服务名称
     * @return
     */
    String serviceName();

    /**
     * hystrix降级逻辑
     * @return
     */
    Class<?>[] fallback() default {};

    String groupKey() default "";

    String commandKey() default "";


}
