package cn.edu.nju.congye6.netcloud.proxy;

import cn.edu.nju.congye6.netcloud.annotation.CloudService;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cong on 2018-10-22.
 */
@CloudService(serviceName = "user")
public interface UserCloudService {

    @RequestMapping("addUser")
    public void addUser();

}
