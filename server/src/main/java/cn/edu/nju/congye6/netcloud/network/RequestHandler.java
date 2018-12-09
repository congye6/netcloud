package cn.edu.nju.congye6.netcloud.network;

import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
import cn.edu.nju.congye6.netcloud.service_register.RpcServiceDispatcher;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by cong on 2018-12-08.
 */
public class RequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER=Logger.getLogger(RequestHandler.class);

    private RpcServiceDispatcher dispatcher=RpcServiceDispatcher.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer=(ByteBuf)msg;
        byte[] data=new byte[buffer.readInt()];
        buffer.readBytes(data);
        RpcRequest request= JSONObject.parseObject(data,RpcRequest.class);

        Map<String,String> headers=request.getHeaders();
        RpcContentType contentType=RpcContentType.JSON;
        if(headers.get("ContentType")!=null)
            contentType=RpcContentType.valueOf(headers.get("ContentType"));

        Channel channel=ctx.channel();
        RpcResponse response=new RpcResponse();
        response.setRequestId(request.getRequestId());
        try{
            Object result=dispatcher.invokeService(request.getRpcId(),request.getParams(),contentType);
            response.setResponse(JSONObject.toJSONString(result));
            response.setSuccess(true);
        }catch (Exception e){
            String message="invoke service error,rpcId:"+request.getRpcId();
            LOGGER.error(message,e);
            response.setSuccess(false);
            response.setResponse(message);
        }
        channel.writeAndFlush(response);
    }


}
