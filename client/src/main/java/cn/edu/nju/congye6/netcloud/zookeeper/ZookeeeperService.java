package cn.edu.nju.congye6.netcloud.zookeeper;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 对外提供zookeeper服务
 * Created by cong on 2018-10-29.
 */
public class ZookeeeperService {

    private static final String NETCLOUD_ROOT_NODE="/netcloud";

    public static boolean exist(String path){
        ZooKeeper zookeeper=ZookeeperManager.getZookeeper();
        try {
            Stat status=zookeeper.exists(path,false);
            return status!=null;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createNode(String path, byte[] data, CreateMode createMode){
        ZooKeeper zookeeper=ZookeeperManager.getZookeeper();
        try {
            zookeeper.create(path,data,null,createMode);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }







}
