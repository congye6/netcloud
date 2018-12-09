package cn.edu.nju.congye6.netcloud.network;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 基于长度的编码器
 * Created by cong on 2018-12-09.
 */
public class ResponseEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] bytes= JSONObject.toJSONBytes(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
