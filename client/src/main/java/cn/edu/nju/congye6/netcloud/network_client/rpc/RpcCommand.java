package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.fuse.FuseCommand;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * Created by cong on 2018-12-31.
 */
public class RpcCommand extends FuseCommand{

    private static final RpcClient RPC_CLIENT =new RpcClient();

    private static final Logger LOGGER= LoggerFactory.getLogger(RpcCommand.class);

    /**
     * 调用参数
     */
    private RpcClientParam param;

    private Method fallback;

    private Object fallbackInstance;

    public RpcCommand(String commadKey, String groupKey,Method fallback,Object fallbackInstance,RpcClientParam param) {
        super(commadKey, groupKey);
        this.param=param;
        this.fallback=fallback;
    }

    public Object invoke() throws Exception{
        Class<?> returnType=param.getReturnType();
        RpcFuture future=excute();
        if(returnType == RpcFuture.class)
            return future;
        return future.get().getResponse(returnType);
    }

    @Override
    protected RpcFuture run() {
        try {
            return RPC_CLIENT.sendAsync(param.getServiceName(),param.getArgs(),param.getAnnotation());
        } catch (Exception e) {
            LOGGER.warn("rpc client invoke exception",e);
        }
        return fallback();
    }

    @Override
    protected RpcFuture fallback() {
        RpcFuture future=new RpcFuture();
        try {
            Object result=fallback.invoke(fallbackInstance,param.getArgs());
            RpcResponse response=new RpcResponse();
            response.setResponse(JSONObject.toJSONString(result));
            future.set(response);
        } catch (Exception e) {
            LOGGER.warn("invoke fallback error",e);
            future.cancel();
        }
        return future;
    }
}
