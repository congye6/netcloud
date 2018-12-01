package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.service_router.AddressDicover;
import cn.edu.nju.congye6.netcloud.zookeeper.RpcServiceChangeWatcher;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 单个服务连接池
 * 获取channel,channel断线重连，channel负载均衡
 * TODO channelPool关闭
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
     * 负载均衡当前的下标
     */
    private AtomicLong currentIndex=new AtomicLong(0);

    /**
     * 线程安全的arraylist
     * 备选：Collections.synchronizedList 写操作性能更好，但是读操作使用了锁，性能比这个差
     * 因为很少需要修改，并且读的次数更多所以选择CopyOnWriteArrayList
     */
    private List<Channel> channels=new CopyOnWriteArrayList<>();

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
     * 没有channel时阻塞
     * get的同时有可能在add
     * @param address
     * @return
     */
    public Channel getChannel(){
        if(channels.isEmpty()){//当前map为空，等待连接成功
            synchronized (this){
                try {
                    this.wait(1000);//超时则唤醒
                } catch (InterruptedException e) {
                    LOGGER.warn("get channel wait,interrupted",e);
                }
            }
        }
        //TODO 此时删除连接，可能出现无法使用的channel
        if(channels.isEmpty())//暂时没有连接
            return null;
        long index=currentIndex.getAndIncrement()%channels.size();
        return channels.get((int)index);
    }

    /**
     * 更新channel
     * 与所有未连接地址建立连接
     * 删除所有已断开的连接
     * @param addressList
     */
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

    /**
     * 添加channel到map和channels
     * 通知所有因为没有channel而阻塞的线程
     * @param address
     * @param channel
     */
    void addChannel(String address, Channel channel){
        channelMap.put(address,channel);
        channels.add(channel);
        synchronized (this){//通知所有因为没有channel而阻塞的线程
            this.notifyAll();
        }
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
        channels.remove(channel);
    }
}
