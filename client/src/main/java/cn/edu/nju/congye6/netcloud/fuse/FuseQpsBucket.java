package cn.edu.nju.congye6.netcloud.fuse;

import java.util.concurrent.atomic.LongAdder;

/**
 * 用于统计qps的分桶
 * Created by cong on 2019-01-04.
 */
public class FuseQpsBucket {

    /**
     * 用于统计所有事件的发生次数，每个事件一个元素
     */
    private final LongAdder[] count;

    /**
     * 开始时间
     */
    private final long startTime;

    FuseQpsBucket(long startTime) {
        this.startTime=startTime;
        count=new LongAdder[FuseEventType.values().length];
        for(LongAdder longAdder:count){
            longAdder=new LongAdder();
        }
    }

    void increment(FuseEventType eventType){
        count[eventType.index()].increment();
    }

    long get(FuseEventType eventType){
        return count[eventType.index()].longValue();
    }

    public long getStartTime() {
        return startTime;
    }
}
