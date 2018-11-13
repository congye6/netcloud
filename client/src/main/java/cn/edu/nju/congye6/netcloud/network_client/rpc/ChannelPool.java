package cn.edu.nju.congye6.netcloud.network_client.rpc;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接池
 * 获取channel,channel断线重连，channel负载均衡
 * Created by cong on 2018-11-13.
 */
public class ChannelPool {

    private Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    private ConnectBuilder builder=new ConnectBuilder();

    private static ChannelPool channelPool=new ChannelPool();

    private ChannelPool(){

    }

    /**
     * 获取连接池实例
     * @return
     */
    public static ChannelPool getInstance(){
        return channelPool;
    }

    /**
     * 根据地址获取channel
     * TODO
     * @param address
     * @return
     */
    public Channel getChannel(String address){

        Channel channel=channelMap.get(address);
        if(channel!=null)
            return channel;

        channel=builder.build(address);
        if(channel!=null)//TODO 并发添加
            channelMap.put(address,channel);
        return channel;
    }
}
