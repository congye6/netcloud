package cn.edu.nju.congye6.netcloud.service_router.load_balancer;

import cn.edu.nju.congye6.netcloud.util.RandomUtil;

import java.util.List;

/**
 * 随机选择地址
 * Created by cong on 2018-10-31.
 */
public class RandomLoadBalancer implements LoadBalancer{
    @Override
    public String next(String serviceName, List<String> addressList) {
        int random= RandomUtil.randomNumber(addressList.size());
        return addressList.get(random);
    }

    @Override
    public void addressChangeNotice(String serviceName, List<String> addressList) {

    }
}
