package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.service_router.AddressDicover;
import cn.edu.nju.congye6.netcloud.zookeeper.RpcServiceChangeWatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 单个服务连接池
 * 获取channel,channel断线重连，channel负载均衡
 * Created by cong on 2018-11-13.
 */
public class ChannelPool {

    private static final Logger LOGGER=Logger.getLogger(ChannelPool.class);

    /**
     * 建立连接类
     */
    private static final ConnectBuilder CONNECT_BUILDER =new ConnectBuilder();

    /**
     * 获取地址列表
     */
    private static final AddressDicover ADDRESS_DICOVER =new AddressDicover();

    /**
     * 管理所有地址的channel
     * 地址-->channel
     */
    private Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 本服务的名称
     */
    private String serviceName;

    public ChannelPool(String serviceName) {
        this.serviceName = serviceName;
        initChannels();
    }

    private void initChannels(){
        RpcServiceChangeWatcher watcher=new RpcServiceChangeWatcher(this);
        List<String> addressList= ADDRESS_DICOVER.getAddressList(serviceName,watcher);
        updateChannel(addressList);
    }

    /**
     * 根据地址获取channel
     * TODO
     * @param address
     * @return
     */
    public Channel getChannel(){
        return null;
    }

    public void updateChannel(List<String> addressList){
        //添加新地址的channel
        for(String address:addressList){
            if(channelMap.containsKey(address))//已存在
                continue;
            //TODO 优化添加回调的方式
            CONNECT_BUILDER.build(address,this);//启动所有连接线程后，异步获取channel
        }

        //删除已断开连接的channel
        for(String address:channelMap.keySet()){
            if(!addressList.contains(address))
                removeChannel(address);
        }
    }

    void addChannel(String address, Channel channel){

    }

    /**
     * 删除一个通道
     * @param address
     */
    public void removeChannel(String address){
        Channel channel=channelMap.get(address);
        if(channel!=null)
            channel.close();
        channelMap.remove(address);
    }
}
