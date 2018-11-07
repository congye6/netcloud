package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.service_router.CloudServiceRouter;
import cn.edu.nju.congye6.netcloud.util.PropertyUtil;

import java.util.*;

/**
 * 使用rpc进行调用
 * Created by cong on 2018-10-24.
 */
public class RpcClient {

    private static final String SOURCE_SERVICE_KEY="cn.edu.nju.congye6.cloudservice.name";

    /**
     * 获取服务地址
     */
    private CloudServiceRouter serviceRouter=CloudServiceRouter.getServiceRouter();



    public Object send(String serviceName, Object[] params, RpcService rpcService){
        Map<String,String> header=new HashMap<>();
        header.put("Source", PropertyUtil.getProperty(SOURCE_SERVICE_KEY));

        String address=serviceRouter.getAddress(serviceName);

        return null;
    }

}
