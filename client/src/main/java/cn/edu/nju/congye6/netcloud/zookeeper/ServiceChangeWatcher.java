package cn.edu.nju.congye6.netcloud.zookeeper;

import cn.edu.nju.congye6.netcloud.service_router.AddressCache;
import cn.edu.nju.congye6.netcloud.service_router.CloudServiceRouter;
import com.alibaba.fastjson.JSONObject;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * 监听子节点变化的watcher
 * Created by cong on 2018-10-31.
 */
public class ServiceChangeWatcher implements Watcher{

    private String serviceName;

    private CloudServiceRouter router;

    public ServiceChangeWatcher(String serviceName,CloudServiceRouter router) {
        this.serviceName = serviceName;
        this.router=router;
    }

    @Override
    public void process(WatchedEvent event) {

        if(Event.EventType.NodeChildrenChanged!=event.getType())//只关心子节点变化
            return;
        List<String> addressList=ZookeeeperService.getChildren(event.getPath(),this);
        router.refreshCache(serviceName,addressList);
        System.out.println(event.getPath()+" update child");
    }
}
