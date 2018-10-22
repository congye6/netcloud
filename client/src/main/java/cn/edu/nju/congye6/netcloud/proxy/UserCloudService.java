package cn.edu.nju.congye6.netcloud.proxy;

import cn.edu.nju.congye6.netcloud.annotation.CloudService;

/**
 * Created by cong on 2018-10-22.
 */
@CloudService(serviceName = "user")
public interface UserCloudService {

    public void addUser();

}
