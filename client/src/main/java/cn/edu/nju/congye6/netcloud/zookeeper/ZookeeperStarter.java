package cn.edu.nju.congye6.netcloud.zookeeper;

import javax.annotation.PostConstruct;

/**
 * Created by cong on 2018-12-14.
 */
public class ZookeeperStarter {


    @PostConstruct
    public void createConnection(){
        ZookeeperManager.createConnection();
    }

}
