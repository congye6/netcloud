package cn.edu.nju.congye6.netcloud.service_router;

import cn.edu.nju.congye6.netcloud.annotation.CloudService;
import cn.edu.nju.congye6.netcloud.zookeeper.ServiceChangeWatcher;
import cn.edu.nju.congye6.netcloud.zookeeper.ZookeeeperService;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取某个服务的可用地址列表
 * Created by cong on 2018-10-30.
 */
public class AddressFinder {

    /**
     * 所有节点的根节点
     */
    private static final String NETCLOUD_ROOT_NODE = "/netcloud/";

    private CloudServiceRouter router;

    public AddressFinder(CloudServiceRouter router) {
        this.router = router;
    }

    public List<String> getAddressList(String serviceName){
        String path=NETCLOUD_ROOT_NODE+serviceName;
        if(!ZookeeeperService.exist(path))
            return new ArrayList<>();
        return ZookeeeperService.getChildren(path,new ServiceChangeWatcher(serviceName,router));
    }


}
