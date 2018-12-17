package cn.edu.nju.congye6.netcloud.zookeeper;

import cn.edu.nju.congye6.netcloud.network_client.rpc.ChannelPool;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 地址改变时通知rpcChannel
 * Created by cong on 2018-11-16.
 */
public class RpcServiceChangeWatcher implements Watcher{

    private static final Logger LOGGER= LoggerFactory.getLogger(RpcServiceChangeWatcher.class);

    /**
     * 更新时通知的连接池
     */
    private ChannelPool channelPool;

    /**
     * 单线程执行更新任务
     */
    private static final Executor UPDATE_EXECUTOR= Executors.newSingleThreadExecutor();

    public RpcServiceChangeWatcher(ChannelPool channelPool) {
        this.channelPool = channelPool;
    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.EventType.NodeChildrenChanged!=event.getType())//只关心子节点变化
            return;
        //TODO 关闭，断开watcher
        List<String> addressList=ZookeeeperService.getChildren(event.getPath(),this);
        //异步执行,防止阻塞zookeeper线程
        //单线程执行,防止更新时正好又有通知
        UPDATE_EXECUTOR.execute(() -> channelPool.updateChannel(addressList));
        LOGGER.info(event.getPath()+" update channel,event:"+event.getType());
    }
}
