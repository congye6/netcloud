package cn.edu.nju.congye6.netcloud.fuse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 熔断器统计信息
 * Created by cong on 2018-12-22.
 */
public class FuseMetrics {

    /**
     * 保存commandkey与metrics的映射
     */
    private static final Map<String,FuseMetrics> INSTANCE_MAP=new ConcurrentHashMap<>();

    /**
     * 根据commandKey获取线程池实例
     * 不存在则新创建一个
     * @param commandKey
     * @return
     */
    static FuseMetrics getInstance(String commandKey){
        FuseMetrics instance=INSTANCE_MAP.putIfAbsent(commandKey,new FuseMetrics());
        if(instance==null){//instance不存在，说明刚创建的对象加入了map
            return INSTANCE_MAP.get(commandKey);
        }else{//instance已经存在
            return instance;
        }
    }


}
