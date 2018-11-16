package cn.edu.nju.congye6.netcloud.service_router;

import cn.edu.nju.congye6.netcloud.annotation.CloudService;
import cn.edu.nju.congye6.netcloud.zookeeper.ServiceChangeWatcher;
import cn.edu.nju.congye6.netcloud.zookeeper.ZookeeeperService;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取某个服务的可用地址列表
 * Created by cong on 2018-10-30.
 */
public class AddressDicover {

    /**
     * 所有节点的根节点
     */
    private static final String NETCLOUD_ROOT_NODE = "/netcloud/";

    private CloudServiceRouter router;

    public AddressDicover(CloudServiceRouter router) {
        this.router = router;
    }

    public AddressDicover() {

    }

    /**
     * 获取地址列表，默认为通知更新地址的watcher
     *
     * @param serviceName
     * @return
     */
    public List<String> getAddressList(String serviceName) {
        return getAddressList(serviceName, new ServiceChangeWatcher(serviceName, router));
    }

    /**
     * 获取地址列表
     * 可选择添加新watcher
     *
     * @param serviceName
     * @param watcher
     * @return
     */
    public List<String> getAddressList(String serviceName, Watcher watcher) {
        String path = NETCLOUD_ROOT_NODE + serviceName;
        if (!ZookeeeperService.exist(path))
            return new ArrayList<>();
        return ZookeeeperService.getChildren(path, watcher);
    }


}
