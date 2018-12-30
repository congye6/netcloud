package cn.edu.nju.congye6.netcloud.fuse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 断路器
 * Created by cong on 2018-12-22.
 */
public class FuseBreaker {

    /**
     * 保存commandkey与断路器的映射
     */
    private static final Map<String, FuseBreaker> INSTANCE_MAP = new ConcurrentHashMap<>();

    /**
     * 统计数据，需要根据统计数据判断是否打开断路器
     */
    private FuseMetrics metrics;

    /**
     * 断路器是否打开
     */
    private volatile boolean isOpen;


    public FuseBreaker(FuseMetrics metrics) {
        this.metrics = metrics;
        this.isOpen = false;
    }

    /**
     * 斷路器是否打開
     * TODO 在打开一段时间后需要允许一次请求
     * @return
     */
    public boolean isOpen(){
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
