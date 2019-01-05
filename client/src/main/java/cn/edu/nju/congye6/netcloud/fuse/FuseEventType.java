package cn.edu.nju.congye6.netcloud.fuse;

/**
 * Created by cong on 2019-01-03.
 */
public enum FuseEventType {
    /**
     * 成功调用
     */
    SUCCESS(0),
    /**
     * 超时
     */
    TIME_OUT(1),
    /**
     * 信号量获取失败
     */
    SEMAPHORE_FAIL(2),
    /**
     * 被断路器隔断
     */
    BREAKER_FAIL(3);

    private int index;

    private FuseEventType(int index){
        this.index=index;
    }

    int index(){
        return index;
    }
}
