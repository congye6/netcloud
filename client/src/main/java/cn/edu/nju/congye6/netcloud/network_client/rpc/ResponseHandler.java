package cn.edu.nju.congye6.netcloud.network_client.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

/**
 * 接收响应，并设置到rpc request
 * ChannelInboundHandlerAdapter处理由decoder编码的RpcRespnse对象
 * Created by cong on 2018-11-13.
 */
public class ResponseHandler extends ChannelInboundHandlerAdapter implements FutureRemovable {

    private static final Logger LOGGER= LoggerFactory.getLogger(ResponseHandler.class);

    /**
     * requestId -> rpcFuture
     * 每个通道一个map，减小竞争
     */
    private Map<String, RpcFuture> responseMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof RpcResponse))
            return;
        RpcResponse rpcResponse=(RpcResponse)msg;
        RpcFuture future=responseMap.get(rpcResponse.getRequestId());//根据requestId获取future
        if(future==null){
            LOGGER.warn("accept reponse without future,requestId:"+rpcResponse.getRequestId());
            return;
        }
        future.success(rpcResponse);//设置响应
        responseMap.remove(rpcResponse.getRequestId());//已经响应，移除future
    }

    /**
     * 发出请求时，添加future
     * @param requestId
     * @param future
     */
    public void addRpcFuture(String requestId,RpcFuture future){
        responseMap.put(requestId,future);
    }


    @Override
    public void removeFuture(String requestId) {
        responseMap.remove(requestId);
    }
}
