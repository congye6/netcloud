package cn.edu.nju.congye6.netcloud;

import cn.edu.nju.congye6.netcloud.annotation.CloudService;
import cn.edu.nju.congye6.netcloud.annotation.EnableCloudClient;
import cn.edu.nju.congye6.netcloud.network_client.request_builder.RequestInterceptor;
import cn.edu.nju.congye6.netcloud.proxy.UserCloudService;
import cn.edu.nju.congye6.netcloud.util.CloudContextUtil;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCloudClient
public class ClientApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context=SpringApplication.run(ClientApplication.class, args);
		CloudContextUtil.setApplicationContext(context);
	}
}
