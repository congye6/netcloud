package cn.edu.nju.congye6.netcloud.fuse;

import java.util.concurrent.atomic.LongAdder;

/**
 * qps计数的快照
 * Created by cong on 2019-01-24.
 */
public class FuseCount {

    private final long[] counts;

    public FuseCount() {
        counts=new long[FuseEventType.values().length];
    }

    public FuseCount(LongAdder[] adders){
        counts=new long[FuseEventType.values().length];
        if(adders.length!=counts.length)
            return;
        for(int i=0;i<adders.length;i++){
            counts[i]=adders[i].longValue();
        }
    }

    /**
     * 增加一个计数
     */
    void increment(FuseCount fuseCount){
        for(int i=0;i<counts.length;i++){
            counts[i]=fuseCount.counts[i]+counts[i];
        }
    }

    /**
     * 获取某个事件的计数
     * @param eventType
     * @return
     */
    long get(FuseEventType eventType){
        return counts[eventType.index()];
    }

    /**
     * 获取计数合计
     * @return
     */
    long getTotal(){
        long total=0;

        for(long count:counts){
            total+=count;
        }

        return total;
    }


}
