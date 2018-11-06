package cn.edu.nju.congye6.netcloud.register;

import cn.edu.nju.congye6.netcloud.zookeeper.ZookeeeperService;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

/**
 * 定时检查是否断开连接
 * Created by cong on 2018-11-06.
 */
public class FailOverTask implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(FailOverTask.class);

    private String path;

    public FailOverTask(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (ZookeeeperService.exist(path))
                continue;
            LOGGER.info("zookeeper node not exist");
            RegisterTask registerTask=new RegisterTask();
            registerTask.run();
            return;
        }
    }
}
