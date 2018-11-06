package cn.edu.nju.congye6.netcloud.register;

import cn.edu.nju.congye6.netcloud.util.IpAddressUtil;
import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
import cn.edu.nju.congye6.netcloud.zookeeper.ZookeeeperService;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import org.springframework.util.StringUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 异步执行注册
 * TODO:上线重连
 * Created by cong on 2018-11-05.
 */
public class RegisterTask implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(RegisterTask.class);

    /**
     * 所有节点的根节点
     */
    private static final String NETCLOUD_ROOT_NODE = "/netcloud";

    private static final String SERVICE_NAME_KEY = "cn.edu.nju.congye6.cloudservice.name";

    private static final String PATH_SPLITER = "/";

    private AddressHelper addressHelper = new AddressHelper();

    @Override
    public void run() {
        //注册根节点
        if (!ZookeeeperService.exist(NETCLOUD_ROOT_NODE))
            ZookeeeperService.createNode(NETCLOUD_ROOT_NODE, null, CreateMode.PERSISTENT);

        //注册服务节点
        String serviceName = PropertyUtil.getProperty(SERVICE_NAME_KEY);
        if (StringUtils.isEmpty(serviceName)) {
            LOGGER.error("未配置服务名称");
            return;
        }
        String servicePath = NETCLOUD_ROOT_NODE + PATH_SPLITER + serviceName;
        if (!ZookeeeperService.exist(servicePath))
            ZookeeeperService.createNode(servicePath, null, CreateMode.PERSISTENT);

        //注册地址节点
        String address = addressHelper.getAddress();
        String path = servicePath + PATH_SPLITER + address;
        ZookeeeperService.createNode(path, null, CreateMode.EPHEMERAL);

        Executor failOverThread = Executors.newSingleThreadExecutor();//后台检查重连
        failOverThread.execute(new FailOverTask(path));
    }


}
