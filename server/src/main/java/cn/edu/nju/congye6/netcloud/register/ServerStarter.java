package cn.edu.nju.congye6.netcloud.register;

import cn.edu.nju.congye6.netcloud.network.NettyServer;
import cn.edu.nju.congye6.netcloud.service_invoker.ServiceImporter;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

/**
 * Created by cong on 2018-12-09.
 */
public class ServerStarter {

    private static final Logger LOGGER=Logger.getLogger(ServerStarter.class);

    @PostConstruct
    public void start() throws Exception {
        LOGGER.info("server starting...");
        ServiceImporter importer=new ServiceImporter();
        importer.selectRpcServices();

        CloudServiceRegister register=new CloudServiceRegister();
        register.register();

        NettyServer nettyServer=new NettyServer();
        nettyServer.startServerInPort();
    }

}
