package cn.edu.nju.congye6.netcloud.service_register;

import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 根据rpcId获取调用的方法
 * Created by cong on 2018-12-05.
 */
public class RpcServiceDispatcher {

    private static final Map<String,RpcServiceProxy> RPC_SERVICE_MAP=new HashMap<>();

    private static RpcServiceDispatcher dispatcher=new RpcServiceDispatcher();

    public static RpcServiceDispatcher getInstance(){
        return dispatcher;
    }

    private RpcServiceDispatcher(){

    }

    /**
     * 添加rpc接口
     * @param rpcId
     * @param rpcServiceProxy
     */
    void addRpcService(String rpcId, RpcServiceProxy rpcServiceProxy){
        if(StringUtils.isEmpty(rpcId))
            return;
        RPC_SERVICE_MAP.put(rpcId,rpcServiceProxy);
    }


    /**
     * 发起rpc调用
     * @param rpcId
     * @param params
     * @param contentType
     * @return  返回被调用方法的返回值，如果没有返回值，返回null
     * @exception IllegalArgumentException  如果参数个数或类型不正确
     * @exception InvocationTargetException 如果调用的方法抛出异常
     */
    public Object invokeService(String rpcId, String[] params, RpcContentType contentType) throws InvocationTargetException, IllegalAccessException{
        RpcServiceProxy proxy=RPC_SERVICE_MAP.get(rpcId);
        if(proxy==null)
            throw new IllegalArgumentException("rpcId不正确");
        return proxy.invoke(params,contentType);
    }


}
