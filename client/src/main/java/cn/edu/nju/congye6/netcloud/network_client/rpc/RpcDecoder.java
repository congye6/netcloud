package cn.edu.nju.congye6.netcloud.network_client.rpc;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 * 对象使用json进行编码，前四字节为长度
 * Created by cong on 2018-11-11.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * 消息的类型，使解码器更通用
     */
    private Class<?> messageType;

    public RpcDecoder(Class<?> messageType) {
        this.messageType = messageType;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes()<4)//小于四字节，无法读到长度
            return;
        int length=in.readInt();
        if(in.readableBytes()<length){//小于对象长度
            in.resetReaderIndex();//从头开始读
            return;
        }
        byte[] data=new byte[length];
        in.readBytes(data);
        out.add(JSONObject.parseObject(data,messageType));
    }
}
