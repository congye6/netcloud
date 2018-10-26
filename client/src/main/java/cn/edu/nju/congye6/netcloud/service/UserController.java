package cn.edu.nju.congye6.netcloud.service;

import cn.edu.nju.congye6.netcloud.proxy.UserCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cong on 2018-10-22.
 */
@RestController
public class UserController {
    @Autowired
    private UserCloudService userService;

    @RequestMapping("/test")
    public String test(){
        User user=new User();

        user.setUserName("cc");
        userService.addUser(user);
        return "success";
    }

    @RequestMapping("/addUser")
    public String addUser(@RequestBody User user){
        System.out.println(user.getUserName());
        return "success";
    }

}
