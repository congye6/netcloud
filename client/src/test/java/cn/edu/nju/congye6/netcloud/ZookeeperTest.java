package cn.edu.nju.congye6.netcloud;

import cn.edu.nju.congye6.netcloud.zookeeper.ServiceChangeWatcher;
import cn.edu.nju.congye6.netcloud.zookeeper.ZookeeeperService;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by cong on 2018-10-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperTest {

    @Test
    public void addNode(){
//        ZookeeeperService.createNode("/test",null, CreateMode.PERSISTENT);
//        System.out.println(ZookeeeperService.exist("/test"));
//        ZookeeeperService.createNode("/test/child",null,CreateMode.EPHEMERAL);
//        ZookeeeperService.createNode("/test/127.0.0.1:8080",null,CreateMode.EPHEMERAL);
//        System.out.println(ZookeeeperService.getChildren("/test",new ServiceChangeWatcher("test",null)));
//        ZookeeeperService.createNode("/test/127.0.0.2:8080",null,CreateMode.EPHEMERAL);
        System.out.println(ZookeeeperService.getChildren("/netcloud/user",null));
    }


}
