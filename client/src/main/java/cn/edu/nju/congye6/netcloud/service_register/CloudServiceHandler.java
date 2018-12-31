package cn.edu.nju.congye6.netcloud.service_register;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.fuse.FuseCommand;
import cn.edu.nju.congye6.netcloud.network_client.http.HttpClient;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcClient;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcClientParam;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcCommand;
import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
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

    private static final String FUSE_ENABLE_KEY="cn.edu.nju.congye6.fuse.enable";

    private static final HttpClient HTTP_CLIENT =new HttpClient();

    private static final RpcClient RPC_CLIENT =new RpcClient();
    /**
     * 调用的服务名称
     */
    private String serviceName;

    /**
     * 降级逻辑
     */
    private Class<?> fallback;

    private Object fallbackInstance;

    private String groupKey;

    private String commandKey;

    public CloudServiceHandler(String serviceName, Class<?> fallback, String groupKey, String commandKey) {
        this.serviceName = serviceName;
        this.fallback=fallback;
        this.groupKey=groupKey;
        this.commandKey=commandKey;
        if(fallback!=null){
            try {
                fallbackInstance=fallback.newInstance();
            } catch (Exception e) {
                LOGGER.warn("create fallback instance fail",e);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //http请求
        RequestMapping requestMapping=method.getAnnotation(RequestMapping.class);
        if(requestMapping!=null)
            return HTTP_CLIENT.send(method.getReturnType(),args,serviceName,requestMapping);

        //rpc请求
        RpcService rpcService=method.getAnnotation(RpcService.class);
        if(rpcService==null){
            LOGGER.warn("invoke method:"+method.getName()+" fail,no annotation");
            return null;
        }

        Boolean isFuseEnable= PropertyUtil.getBooleanProperty(FUSE_ENABLE_KEY);
        if(isFuseEnable==null||!isFuseEnable){//未打开熔断器
            return RPC_CLIENT.send(serviceName,args,rpcService,method.getReturnType());
        }

        //熔断器监控执行
        RpcClientParam params=new RpcClientParam(serviceName,args,rpcService,method.getReturnType());
        Method fallbackMethod=fallback.getMethod(method.getName(),method.getParameterTypes());
        RpcCommand rpcCommand=new RpcCommand(commandKey,groupKey,fallbackMethod,fallbackInstance,params);
        return rpcCommand.invoke();
    }



    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
