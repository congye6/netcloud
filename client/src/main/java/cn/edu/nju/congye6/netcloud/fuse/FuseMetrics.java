package cn.edu.nju.congye6.netcloud.fuse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 熔断器统计信息
 * Created by cong on 2018-12-22.
 */
public class FuseMetrics {

    private static final Logger LOGGER= LoggerFactory.getLogger(FuseMetrics.class);

    /**
     * 保存commandkey与metrics的映射
     */
    private static final Map<String,FuseMetrics> INSTANCE_MAP=new ConcurrentHashMap<>();


    private FuseRollingNumber qpsCounter;


    private FuseMetrics(){
        qpsCounter=new FuseRollingNumber();
    }

    void breakerFail(){
        LOGGER.info("breaker fail count");
        qpsCounter.increment(FuseEventType.BREAKER_FAIL);
    }

    void semaphoreFail(){
        LOGGER.info("semaphore fail count");
        qpsCounter.increment(FuseEventType.SEMAPHORE_FAIL);
    }

    public void exception(){
        LOGGER.info("exception count");
        qpsCounter.increment(FuseEventType.EXCEPTION);
    }

    public void success(){
        LOGGER.info("success count");
        qpsCounter.increment(FuseEventType.SUCCESS);
    }

    void timeout(){
        LOGGER.info("timeout count");
        qpsCounter.increment(FuseEventType.TIME_OUT);
    }

    /**
     * 获取成功次数
     * @return
     */
    long getSuccessCount(){
        return qpsCounter.count(FuseEventType.SUCCESS);
    }

    /**
     * 获取总调用次数
     * @return
     */
    long getTotal(){
        FuseCount sum=qpsCounter.count();
        return sum.getTotal();
    }

    /**
     * 根据commandKey获取线程池实例
     * 不存在则新创建一个
     * @param commandKey
     * @return
     */
    static FuseMetrics getInstance(String commandKey){
        FuseMetrics instance=INSTANCE_MAP.get(commandKey);
        if(instance!=null)
            return instance;
        instance=INSTANCE_MAP.putIfAbsent(commandKey,new FuseMetrics());
        if(instance==null){//instance不存在，说明刚创建的对象已加入map
            return INSTANCE_MAP.get(commandKey);
        }else{//instance未存在，加入失败
            return instance;
        }
    }

}
