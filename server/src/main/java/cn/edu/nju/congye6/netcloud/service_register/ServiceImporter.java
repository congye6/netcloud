package cn.edu.nju.congye6.netcloud.service_register;

import cn.edu.nju.congye6.netcloud.annotation.RpcCloudService;
import cn.edu.nju.congye6.netcloud.util.CloudContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.reflect.*;

/**
 * 将注册了RpcCloudService的方法进行注册
 * Created by cong on 2018-12-05.
 */
public class ServiceImporter{

    private RpcServiceDispatcher dispatcher=RpcServiceDispatcher.getInstance();

    public void selectRpcServices(){
        List<Object> controllers=getControllers();
        for(Object controller:controllers){
            Class<?> controllerClazz=controller.getClass();
            Method[] methods=controllerClazz.getDeclaredMethods();
            for(Method method:methods){
                RpcCloudService annotation=method.getAnnotation(RpcCloudService.class);
                if(annotation==null)
                    continue;
                RpcServiceProxy proxy=new RpcServiceProxy(method,controller);//默认为单例模式
                dispatcher.addRpcService(annotation.rpcId(),proxy);
            }
        }
    }

    /**
     * 获取所有被注解的controller
     * @return
     */
    private List<Object> getControllers(){
        List<Object> controllers=new ArrayList<>();
        ApplicationContext context= CloudContextUtil.getContext();
        //获取被@Controller注解的类,@RestController也是被被@Controller注解的类
        Map<String,Object> beanMap=context.getBeansWithAnnotation(Controller.class);//添加被Controller注解的类
        if(beanMap.values()!=null)
            controllers.addAll(beanMap.values());
        return controllers;
    }


}
