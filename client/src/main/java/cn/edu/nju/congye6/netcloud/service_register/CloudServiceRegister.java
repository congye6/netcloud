package cn.edu.nju.congye6.netcloud.service_register;

import cn.edu.nju.congye6.netcloud.annotation.CloudService;
import cn.edu.nju.congye6.netcloud.util.CloudContextUtil;
import cn.edu.nju.congye6.netcloud.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * 搜索所有@CloudService注解的类
 * 在@EnableCloudService中@import
 * Created by cong on 2018-10-19.
 */
public class CloudServiceRegister implements ImportBeanDefinitionRegistrar {

    private static final Logger LOGGER= LoggerFactory.getLogger(CloudServiceRegister.class);

    private PackageUtil packageUtil=new PackageUtil();


    /**
     * 获取basePackage下所有class，筛选被注解的接口
     * 生成对象注入到spring工厂中管理
     * annotationMetadata可以获取到被注解的类的信息，一般注解在启动类上
     * @return
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        String basePackage= ClassUtils.getPackageName(annotationMetadata.getClassName());
        List<Class<?>> allClass=packageUtil.getClass(basePackage,true);
        for(Class<?> clazz:allClass){
            CloudService cloudServiceAnnotation=clazz.getAnnotation(CloudService.class);
            if(cloudServiceAnnotation==null)//没有被注解
                continue;
            registerCloudService(clazz,cloudServiceAnnotation,beanDefinitionRegistry);
        }
    }

    /**
     * 注册bean
     * 参考自springcloud feign
     * @param interfaceName
     * @param annotation
     * @param registry
     */
    private void registerCloudService(Class<?> interfaceClass,CloudService annotation,BeanDefinitionRegistry registry){
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(CloudServiceBean.class);
        //设置注解上的属性
        LOGGER.info("register service:"+annotation.serviceName());
        definitionBuilder.addPropertyValue("serviceName", annotation.serviceName());
        definitionBuilder.addPropertyValue("type",interfaceClass);
        addFallback(annotation.fallback(), definitionBuilder);
        definitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition beanDefinition=definitionBuilder.getBeanDefinition();
        beanDefinition.setPrimary(true);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, interfaceClass.getCanonicalName(), new String[]{});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private void addFallback(Class<?>[] fallbacks, BeanDefinitionBuilder definitionBuilder) {
        Class<?> fallback=null;
        if(fallbacks.length>0){
            fallback=fallbacks[0];
        }
        definitionBuilder.addPropertyValue("fallback",fallback);
    }

}
