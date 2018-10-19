package cn.edu.nju.congye6.netcloud.service_register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索所有@CloudService注解的类
 * Created by cong on 2018-10-19.
 */
@Component
public class CloudServiceSearcher {

    private PackageUtil packageUtil=new PackageUtil();

    @Value("${cn.edu.nju.congye6.cloud.package}")
    private String basePackage;

    /**
     * 获取basePackage下所有class，筛选被注解的接口
     * @return
     */
    public List<Class<?>> getAnnotatedInterface(){
        List<Class<?>> allClass=packageUtil.getClass(basePackage,true);

        List<Class<?>> annotatedClasses=new ArrayList<>();


        return  annotatedClasses;
    }


}
