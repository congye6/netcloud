package cn.edu.nju.congye6.netcloud.zookeeper;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.network_client.rpc.ChannelPool;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * 地址改变时通知rpcChannel
 * Created by cong on 2018-11-16.
 */
public class RpcServiceChangeWatcher implements Watcher{

    private static final Logger LOGGER=Logger.getLogger(RpcServiceChangeWatcher.class);

    /**
     * 更新时通知的连接池
     */
    private ChannelPool channelPool;

    public RpcServiceChangeWatcher(ChannelPool channelPool) {
        this.channelPool = channelPool;
    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.EventType.NodeChildrenChanged!=event.getType())//只关心子节点变化
            return;
        //TODO 更新时正好又有通知
        List<String> addressList=ZookeeeperService.getChildren(event.getPath(),this);
        LOGGER.info(event.getPath()+" update channel");
    }
}
