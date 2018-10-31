package cn.edu.nju.congye6.netcloud.service_router.load_balancer;

import java.util.List;

/**
 * 轮询
 * Created by cong on 2018-10-31.
 */
public class RoundRobinLoadBalancer implements LoadBalancer{
    @Override
    public String next(String serviceName, List<String> addressList) {
        return null;
    }

    @Override
    public void addressChangeNotice(String serviceName, List<String> addressList) {

    }
}
