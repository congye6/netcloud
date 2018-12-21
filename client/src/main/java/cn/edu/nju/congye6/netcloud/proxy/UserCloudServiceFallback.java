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
        return null;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public RpcFuture addUser(User user, List<RpcCallBack> callBacks) {
        return null;
    }
}
