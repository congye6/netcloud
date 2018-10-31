package cn.edu.nju.congye6.netcloud.service_router.load_balancer;

import java.util.List;

/**
 * 负载均衡策略
 * Created by cong on 2018-10-31.
 */
public interface LoadBalancer {

    /**
     * 根据策略获取下一个请求的地址
     * @param serviceName
     * @param addressList
     * @return
     */
    public String next(String serviceName,List<String> addressList);

    /**
     * 通知地址列表更新
     * 下一个请求可能有变化
     * @param serviceName
     */
    public void addressChangeNotice(String serviceName,List<String> addressList);

}
