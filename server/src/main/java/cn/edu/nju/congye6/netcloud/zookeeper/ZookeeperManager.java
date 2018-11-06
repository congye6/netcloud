package cn.edu.nju.congye6.netcloud.zookeeper;

import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by cong on 2018-01-16.
 */
public class ZookeeperManager {


    private static ZooKeeper zookeeper;

    private static final String ZOOKEEPER_HOST_LIST_KEY="cn.edu.nju.congye6.zookeeper.host";

    private static final int SESSION_TIME_OUT=2000;

    private static CountDownLatch countDownLatch=new CountDownLatch(1);

    public static void createConnection(){
        Watcher watcher=new ConnectionWatcher(countDownLatch);
        try {
            zookeeper=new ZooKeeper(PropertyUtil.getProperty(ZOOKEEPER_HOST_LIST_KEY),SESSION_TIME_OUT,watcher);
            countDownLatch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void close(){
        if(zookeeper!=null){
            try {
                zookeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static ZooKeeper getZookeeper(){
        if(zookeeper!=null){
            if(zookeeper.getState().isAlive()){
                return zookeeper;
            }
            close();
        }
        createConnection();
        return zookeeper;
    }

}
