package cn.edu.nju.congye6.netcloud.service_router;

import cn.edu.nju.congye6.netcloud.service_router.load_balancer.LoadBalancer;
import cn.edu.nju.congye6.netcloud.service_router.load_balancer.RoundRobinLoadBalancer;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 查找服务地址
 * 服务地址缓存
 * 服务地址负载均衡
 * Created by cong on 2018-10-30.
 */
public class CloudServiceRouter{

    /**
     * 负载均衡器
     */
    private LoadBalancer loadBalancer;

    /**
     * 地址缓存
     */
    private AddressCache addressCache;

    /**
     * 地址获取
     */
    private AddressFinder addressFinder;

    public  CloudServiceRouter(){
        loadBalancer=new RoundRobinLoadBalancer();
        addressCache=new AddressCache();
        addressFinder=new AddressFinder(this);
    }

    /**
     * 获取地址
     * @param serviceName
     * @return
     */
    public String getAddress(String serviceName){
        List<String> addressList=addressCache.getAddressList(serviceName);
        if(!CollectionUtils.isEmpty(addressList))//缓存中存在
            return loadBalancer.next(serviceName,addressList);
        addressList=addressFinder.getAddressList(serviceName);
        if(CollectionUtils.isEmpty(addressList))//找不到地址
            return null;
        addressCache.updateAddress(serviceName,addressList);//保存缓存
        return loadBalancer.next(serviceName,addressList);
    }

    /**
     * 刷新缓存
     * @param serviceName
     * @param addressList
     */
    public void refreshCache(String serviceName,List<String> addressList){
        addressCache.updateAddress(serviceName,addressList);
        loadBalancer.addressChangeNotice(serviceName,addressList);
    }




}
