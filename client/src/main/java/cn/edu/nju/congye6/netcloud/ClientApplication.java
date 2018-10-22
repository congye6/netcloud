package cn.edu.nju.congye6.netcloud;

import cn.edu.nju.congye6.netcloud.annotation.EnableCloudService;
import cn.edu.nju.congye6.netcloud.service_register.PackageUtil;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCloudService
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
}
