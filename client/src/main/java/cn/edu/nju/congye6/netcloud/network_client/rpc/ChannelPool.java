package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.service_router.AddressDicover;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单个服务连接池
 * 获取channel,channel断线重连，channel负载均衡
 * Created by cong on 2018-11-13.
 */
public class ChannelPool {

    /**
     * 管理所有地址的channel
     * 地址-->channel
     */
    private Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 建立连接类
     */
    private ConnectBuilder builder=new ConnectBuilder();

    /**
     * 获取地址列表
     */
    private AddressDicover addressDicover=new AddressDicover();

    /**
     * 本服务的名称
     */
    private String serviceName;

    public ChannelPool(String serviceName) {
        this.serviceName = serviceName;
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

    public void updateChannel(List<String> addressList){

    }
}
