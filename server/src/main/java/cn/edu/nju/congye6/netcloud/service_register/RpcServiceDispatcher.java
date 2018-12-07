package cn.edu.nju.congye6.netcloud.service_register;

import org.springframework.util.StringUtils;

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
        System.out.println("rpcId:"+rpcId);
        RPC_SERVICE_MAP.put(rpcId,rpcServiceProxy);
    }



}
