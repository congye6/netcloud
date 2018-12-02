package cn.edu.nju.congye6.netcloud.proxy;

import cn.edu.nju.congye6.netcloud.annotation.CloudService;
import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcFuture;
import cn.edu.nju.congye6.netcloud.service.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by cong on 2018-10-22.
 */
@CloudService(serviceName = "user")
public interface UserCloudService {

    /*@RequestMapping(value = "/addUser")
    public String addUser(User user);*/

    @RpcService(rpcId = "user.getUser",contentType = RpcContentType.PLAIN)
    public User getUser(String username);

    @RpcService(rpcId = "user.addUser")
    public RpcFuture addUser(User user);

}
