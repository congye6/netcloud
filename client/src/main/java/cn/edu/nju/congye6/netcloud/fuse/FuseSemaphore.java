package cn.edu.nju.congye6.netcloud.fuse;

import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 信号量，用于限制一个服务组的并发请求数
 * Created by cong on 2018-12-30.
 */
public class FuseSemaphore {

    private static  final Logger LOGGER= LoggerFactory.getLogger(FuseSemaphore.class);

    /**
     * 默认最大的允许并发数
     */
    private static final int DEFAULT_REQUEST_PERMITTED=30;

    private static final String REUQEST_PERMITTED_KEY="cn.edu.nju.congye6.fuse.request.max";

    private static final Map<String,FuseSemaphore> SEMAPHORE_MAP=new ConcurrentHashMap<>();

    /**
     * 总共允许的请求数
     */
    private int requestPermited;

    /**
     * 当前的请求数,递增和递减都保证原子性
     */
    private AtomicInteger requestCount=new AtomicInteger(0);

    public FuseSemaphore() {
        String requestPermitedConfig=PropertyUtil.getProperty(REUQEST_PERMITTED_KEY);
        try{
            this.requestPermited = Integer.parseInt(requestPermitedConfig);
        }catch (Exception e){
            LOGGER.warn("error config of fuse max request");
            this.requestPermited=DEFAULT_REQUEST_PERMITTED;
        }

    }


    /**
     * 判断能否进行请求
     * @return
     */
    public boolean tryAcquire(){
        int count=requestCount.incrementAndGet();
        if(count>requestPermited){
            requestCount.decrementAndGet();
            return false;
        }
        return true;
    }

    /**
     * 完成请求
     */
    public void release(){
        requestCount.decrementAndGet();
    }

    /**
     * 根据groupkey获取信号量实例
     * @param groupKey
     * @param requestPermited
     * @return
     */
    static FuseSemaphore getSemaphore(String groupKey){
        FuseSemaphore semaphore=SEMAPHORE_MAP.get(groupKey);
        if(semaphore!=null)
            return semaphore;
        FuseSemaphore newSemaphore=new FuseSemaphore();
        semaphore=SEMAPHORE_MAP.putIfAbsent(groupKey,newSemaphore);//并发添加新的信号量
        if(semaphore!=null)//信号量已存在
            return semaphore;
        return newSemaphore;
    }


}
