package cn.edu.nju.congye6.netcloud.service_register;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.network_client.http.HttpClient;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 远程服务的代理类
 * Created by cong on 2018-10-23.
 */
public class CloudServiceHandler implements InvocationHandler{

    private HttpClient httpClient=new HttpClient();

    private RpcClient rpcClient=new RpcClient();
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
            return httpClient.send(method.getReturnType(),args,serviceName,requestMapping);

        //rpc请求
        RpcService rpcService=method.getAnnotation(RpcService.class);
        if(rpcService!=null)
            return rpcClient.send(serviceName,args,rpcService);

        return null;
    }



    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
