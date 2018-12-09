package cn.edu.nju.congye6.netcloud.service;

import cn.edu.nju.congye6.netcloud.annotation.RpcCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cong on 2018-10-22.
 */
@RestController
public class UserController {


    @RequestMapping("/addUser")
    public String addUser(@RequestBody User user){
        System.out.println(user.getUserName());
        return "success";
    }

    @RpcCloudService(rpcId = "user.getUser")
    public User getUser(String username){
        User user=new User();
        user.setUserName(username);
        user.setId("1111");
        return user;
    }

}
