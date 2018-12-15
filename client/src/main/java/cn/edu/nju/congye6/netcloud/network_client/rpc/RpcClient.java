package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.network_client.request_builder.RpcRequestBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使用rpc进行调用
 * Created by cong on 2018-10-24.
 */
public class RpcClient {

    private static final Logger LOGGER= LoggerFactory.getLogger(RpcClient.class);

    private static final long MAX_TIME_OUT=10;


    /**
     * 连接池
     */
    private ChannelPoolManager channelPoolManager = new ChannelPoolManager();


    public Object send(String serviceName, Object[] params, RpcService rpcService,Class<?> returnType) throws Exception {
        RpcFuture future=new RpcFuture();
        CallBackBuilder.buildCallBack(rpcService,future,params);

        RpcRequestBuilder requestBuilder=new RpcRequestBuilder();
        RpcRequest request=requestBuilder.build(rpcService,params);

        //发起调用
        ChannelPool channelPool=channelPoolManager.getChannelPool(serviceName);
        Channel channel = channelPool.getChannel();//获取channel
        if(channel==null){
            LOGGER.error("连接超时,没有可用channel");
            throw new Exception("连接超时,没有可用channel");
        }
        ResponseHandler responseHandler=channel.pipeline().get(ResponseHandler.class);
        responseHandler.addRpcFuture(request.getRequestId(),future);//设置future，以便异步获取结果
        ChannelFuture channelFuture=channel.writeAndFlush(request);
        future.setRequestFuture(channelFuture);
        if(RpcFuture.class==returnType)//要求返回future
            return future;
        //否则同步获取结果,解析json;设置超时时间
        RpcResponse response=future.get(MAX_TIME_OUT, TimeUnit.SECONDS);
        return response.getResponse(returnType);
    }



}
