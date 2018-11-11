package cn.edu.nju.congye6.netcloud.network_client.rpc;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * 前四字节为长度，后面用json序列化
 * Created by cong on 2018-11-11.
 */
public class RpcEncoder extends MessageToByteEncoder{
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] data= JSONObject.toJSONBytes(msg);
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}
