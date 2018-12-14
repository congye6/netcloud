package cn.edu.nju.congye6.netcloud;

import cn.edu.nju.congye6.netcloud.annotation.EnableCloudServer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by cong on 2018-12-12.
 */
@EnableCloudServer
@Configuration
public class ServerApplication {

    private static final Logger LOGGER=Logger.getLogger(ServerApplication.class);



    public static void main(String[] args){
        LOGGER.info("server starting...");
        ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
        System.out.println(context.getEnvironment().getProperty("cn.edu.nju.congye6.rpc.port"));;
    }

}
