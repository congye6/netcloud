package cn.edu.nju.congye6.netcloud;

import cn.edu.nju.congye6.netcloud.annotation.EnableCloudClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCloudClient
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
}
