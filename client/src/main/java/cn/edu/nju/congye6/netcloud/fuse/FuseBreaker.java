package cn.edu.nju.congye6.netcloud.fuse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 断路器
 * Created by cong on 2018-12-22.
 */
public class FuseBreaker {

    /**
     * 默认qps阈值
     */
    private static final long DEFAULT_QPS_THRESHOLD=50;

    /**
     * 默认成功率阈值
     */
    private static final int DEFAULT_SUCCESS_PERCENT_THRESHOLD=80;

    /**
     * 保存commandkey与断路器的映射
     */
    private static final Map<String, FuseBreaker> INSTANCE_MAP = new ConcurrentHashMap<>();

    /**
     * 统计数据，需要根据统计数据判断是否打开断路器
     */
    private FuseMetrics metrics;

    /**
     * 打开断路器的qps阈值
     */
    private final long qpsThreshold;

    /**
     * 打开断路器的成功率阈值
     */
    private final int successPercentThreshold;

    /**
     * 断路器是否打开
     */
    private volatile boolean isOpen;

    /**
     * 上次断路器打开的时间
     */
    private long openTime;

    /**
     * 打开断路器后，间隔一段时间允许尝试一次
     */
    private static final int OPEN_INTERVAL=5000;

    public FuseBreaker(FuseMetrics metrics) {
        this.metrics = metrics;
        this.isOpen = false;
        openTime=System.currentTimeMillis();
        qpsThreshold=getQpsThreshold();
        successPercentThreshold=getSuccessPercentThreshold();
    }

    private long getQpsThreshold(){
        //TODO 属性配置
        return DEFAULT_QPS_THRESHOLD;
    }

    private int getSuccessPercentThreshold(){
        return DEFAULT_SUCCESS_PERCENT_THRESHOLD;
    }

    /**
     * 斷路器是否打開
     * 在打开一段时间后需要允许一次请求
     * @return
     */
    public boolean isOpen(){
        if(isOpen){
            long interval=System.currentTimeMillis()-openTime;
            if(interval<OPEN_INTERVAL)
                return isOpen;
            return false;
        }

        long qps=metrics.getTotal();
        long success=metrics.getSuccessCount();
        if(qps>=qpsThreshold&&success/qps<successPercentThreshold){//两个条件同时满足
            isOpen=true;
            openTime=System.currentTimeMillis();
            return isOpen;
        }
        isOpen=false;
        return isOpen;
    }


    /**
     * 根据commandKey获取断路器
     * 没有则新建一个
     *
     * @param commandKey
     * @param metrics
     * @return
     */
    static FuseBreaker getInstance(String commandKey, FuseMetrics metrics) {
        FuseBreaker instance=INSTANCE_MAP.get(commandKey);
        if(instance!=null)//instance已存在
            return instance;
        instance = INSTANCE_MAP.putIfAbsent(commandKey, new FuseBreaker(metrics));
        if (instance == null) {//instance不存在，说明刚创建的对象加入了map
            return INSTANCE_MAP.get(commandKey);
        } else {//instance已经存在
            return instance;
        }
    }


}
