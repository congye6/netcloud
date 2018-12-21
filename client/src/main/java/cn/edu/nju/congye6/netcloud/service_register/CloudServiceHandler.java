package cn.edu.nju.congye6.netcloud.service_register;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.network_client.http.HttpClient;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 远程服务的代理类
 * Created by cong on 2018-10-23.
 */
public class CloudServiceHandler implements InvocationHandler{

    private static final Logger LOGGER= LoggerFactory.getLogger(CloudServiceHandler.class);

    private static final HttpClient HTTP_CLIENT =new HttpClient();

    private static final RpcClient RPC_CLIENT =new RpcClient();
    /**
     * 调用的服务名称
     */
    private String serviceName;

    public CloudServiceHandler(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //http请求
        RequestMapping requestMapping=method.getAnnotation(RequestMapping.class);
        if(requestMapping!=null)
            return HTTP_CLIENT.send(method.getReturnType(),args,serviceName,requestMapping);

        //rpc请求
        RpcService rpcService=method.getAnnotation(RpcService.class);
        if(rpcService!=null)
            return RPC_CLIENT.send(serviceName,args,rpcService,method.getReturnType());

        LOGGER.warn("invoke method:"+method.getName()+" fail,no annotation");
        return null;
    }



    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
