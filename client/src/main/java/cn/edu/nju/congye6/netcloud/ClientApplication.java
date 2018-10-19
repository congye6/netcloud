package cn.edu.nju.congye6.netcloud;

import cn.edu.nju.congye6.netcloud.service_register.PackageUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
		System.out.println(new PackageUtil().getClass("cn.edu.nju.congye6.netcloud",true));
	}
}
