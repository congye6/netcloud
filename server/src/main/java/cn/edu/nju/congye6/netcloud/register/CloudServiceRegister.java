package cn.edu.nju.congye6.netcloud.register;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import javax.annotation.PostConstruct;

public class CloudServiceRegister{
    @PostConstruct
    public void register(){
        Thread registerThread=new Thread(new RegisterTask());
        registerThread.start();
    }
}