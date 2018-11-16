package cn.edu.nju.congye6.netcloud.zookeeper;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * 对外提供zookeeper服务
 * Created by cong on 2018-10-29.
 */
public class ZookeeeperService {

    /**
     * 判断是否节点存在
     * 不添加watcher
     *
     * @param path
     * @return
     */
    public static boolean exist(String path) {
        ZooKeeper zookeeper = ZookeeperManager.getZookeeper();
        try {
            Stat status = zookeeper.exists(path, false);
            return status != null;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建节点
     * 不添加acl权限
     * @param path
     * @param data
     * @param createMode
     */
    public static void createNode(String path, byte[] data, CreateMode createMode) {
        ZooKeeper zookeeper = ZookeeperManager.getZookeeper();
        try {
            zookeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取子节点
     * @param path
     * @return
     */
    public static List<String> getChildren(String path, Watcher watcher) {
        ZooKeeper zookeeper = ZookeeperManager.getZookeeper();
        try {
            return zookeeper.getChildren(path,watcher);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
