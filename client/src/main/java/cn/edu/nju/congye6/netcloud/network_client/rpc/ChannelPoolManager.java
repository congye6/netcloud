package cn.edu.nju.congye6.netcloud.network_client.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

/**
 * 管理所有服务的连接池
 * TODO 连接的lru
 * TODO 断开长时间未用连接
 * Created by cong on 2018-11-16.
 */
public class ChannelPoolManager {

    private static final Logger LOGGER= LoggerFactory.getLogger(ChannelPoolManager.class);

    /**
     * 管理所有服务的连接池
     * 服务-->channelPool
     */
    private Map<String,ChannelPool> channelPoolMap=new ConcurrentHashMap<>();

    /**
     * 根据服务名获取
     * @param serviceName
     * @return
     */
    public ChannelPool getChannelPool(String serviceName){
        ChannelPool channelPool=channelPoolMap.get(serviceName);
        if(channelPool==null){//pool不存在
            synchronized (this){//加锁，防止并发添加
                channelPool=channelPoolMap.get(serviceName);
                if(channelPool==null){
                    LOGGER.info("creating channel pool of "+serviceName);
                    channelPool=new ChannelPool(serviceName);
                    channelPoolMap.put(serviceName,channelPool);
                    LOGGER.info("channel pool of "+serviceName+" created");
                }
            }
        }
        return channelPool;
    }


}
