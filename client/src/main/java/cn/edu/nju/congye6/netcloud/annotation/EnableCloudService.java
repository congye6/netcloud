package cn.edu.nju.congye6.netcloud.annotation;

import cn.edu.nju.congye6.netcloud.service_register.CloudServiceRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cong on 2018-10-22.
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.TYPE})//定义注解的作用目标**作用范围字段、枚举的常量/方法
@Import(CloudServiceRegister.class)
public @interface EnableCloudService {
}
