package cn.edu.nju.congye6.netcloud.service_router.load_balancer;

import cn.edu.nju.congye6.netcloud.util.RandomUtil;

import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询
 * 加锁操作
 * Created by cong on 2018-10-31.
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    /**
     * 保存上次分配了第几个地址
     */
    private Map<String,Integer> ADDRESS_INDEX_MAP=new HashMap<>();

    @Override
    public synchronized String next(String serviceName, List<String> addressList) {
        //存在并发操作，需要加锁
        //没有耗时操作
        Integer index=ADDRESS_INDEX_MAP.get(serviceName);
        if(index==null)
            index=-1;
        index=(index+1)%addressList.size();
        ADDRESS_INDEX_MAP.put(serviceName,index);
        return addressList.get(index);
    }

    @Override
    public synchronized void addressChangeNotice(String serviceName, List<String> addressList) {
        ADDRESS_INDEX_MAP.put(serviceName, RandomUtil.randomNumber(addressList.size()));
    }
}
