package cn.edu.nju.congye6.netcloud.service;

import cn.edu.nju.congye6.netcloud.proxy.UserCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by cong on 2018-10-22.
 */
@Controller
public class UserController {
    @Autowired
    private UserCloudService userService;

}
