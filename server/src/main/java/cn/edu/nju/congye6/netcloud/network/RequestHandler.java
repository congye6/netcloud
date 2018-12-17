package cn.edu.nju.congye6.netcloud.network;

import cn.edu.nju.congye6.netcloud.service_invoker.RpcServiceDispatcher;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

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
        dispatcher.invokeServiceAsync(request,ctx);
        LOGGER.info("invoke service success");
    }


}
