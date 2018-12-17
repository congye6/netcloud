package cn.edu.nju.congye6.netcloud.service_invoker;

import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
import cn.edu.nju.congye6.netcloud.network.RpcRequest;
import cn.edu.nju.congye6.netcloud.network.RpcResponse;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 根据rpcId获取调用的方法
 * Created by cong on 2018-12-05.
 */
public class RpcServiceDispatcher {

    private static final Logger LOGGER=Logger.getLogger(RpcServiceDispatcher.class);

    /**
     * 所有可以远程调用的方法
     */
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
     * 异步发起rpc调用
     * @param request
     * @param ctx
     */
    public void invokeServiceAsync(RpcRequest request, ChannelHandlerContext ctx){
        InvokerThreadPool threadPool=InvokerThreadPool.getInstance();
        threadPool.execute(()->invokeService(request,ctx));
    }

    /**
     * 同步发起rpc调用
     * @response  返回被调用方法的返回值，如果没有返回值，返回null
     * @exception IllegalArgumentException  如果参数个数或类型不正确
     * @exception InvocationTargetException 如果调用的方法抛出异常
     */
    public void invokeService(RpcRequest request, ChannelHandlerContext ctx){
        RpcServiceProxy proxy=RPC_SERVICE_MAP.get(request.getRpcId());
        Channel channel=ctx.channel();
        RpcResponse response=new RpcResponse();
        try{
            if(proxy==null)
                throw new IllegalArgumentException("rpcId不正确");
            Map<String,String> headers=request.getHeaders();
            RpcContentType contentType=RpcContentType.JSON;
            if(headers.get("ContentType")!=null)
                contentType=RpcContentType.valueOf(headers.get("ContentType"));
            response.setRequestId(request.getRequestId());
            LOGGER.info("rpcId:"+request.getRpcId()+" requestId:"+request.getRequestId());
            Object result=proxy.invoke(request.getParams(),contentType);
            response.setResponse(JSONObject.toJSONString(result));
            response.setSuccess(true);
            LOGGER.info("response:"+response.getResponse());
        }catch (Exception e){
            String message="invoke service error,rpcId:"+request.getRpcId();
            LOGGER.error(message,e);
            response.setSuccess(false);
            response.setResponse(message);
        }
        channel.writeAndFlush(response);
    }


}
