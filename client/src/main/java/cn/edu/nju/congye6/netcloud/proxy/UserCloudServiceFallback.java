package cn.edu.nju.congye6.netcloud.proxy;

import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcCallBack;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcFuture;
import cn.edu.nju.congye6.netcloud.service.User;

import java.util.List;

/**
 * Created by cong on 2018-12-21.
 */
public class UserCloudServiceFallback implements UserCloudService{
    @Override
    public String addUser(User user) {
        return "add user may be fail";
    }

    @Override
    public User getUser(String username) {
        return new User();
    }

    @Override
    public RpcFuture addUser(User user, List<RpcCallBack> callBacks) {
        return null;
    }
}
