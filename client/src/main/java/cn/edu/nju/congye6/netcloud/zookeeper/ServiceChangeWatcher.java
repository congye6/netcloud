package cn.edu.nju.congye6.netcloud.zookeeper;

import cn.edu.nju.congye6.netcloud.service_router.AddressCache;
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

    public ServiceChangeWatcher(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public void process(WatchedEvent event) {

        if(Event.EventType.NodeChildrenChanged!=event.getType())//只关心子节点变化
            return;
        AddressCache addressCache=new AddressCache();
        List<String> addressList=ZookeeeperService.getChildren(event.getPath(),this);
        addressCache.updateAddress(serviceName,addressList);
        System.out.println(event.getPath()+" update child");
    }
}
