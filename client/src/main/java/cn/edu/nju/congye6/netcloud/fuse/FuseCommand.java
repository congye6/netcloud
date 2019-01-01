package cn.edu.nju.congye6.netcloud.fuse;

import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcFuture;

/**
 * 熔断器命令基类
 * Created by cong on 2018-12-22.
 */
public abstract class FuseCommand {

    /**
     * 断路器，每个command一个
     */
    protected final FuseBreaker breaker;

    /**
     * 信号量，每个group一个
     */
    protected final FuseSemaphore semaphore;

    /**
     * 统计信息，每个command一个
     */
    protected final FuseMetrics metrics;

    /**
     * 命令的key
     */
    protected final String commadKey;

    /**
     * 分组的key
     */
    protected final String groupKey;

    protected FuseCommand(FuseBreaker breaker, FuseSemaphore semaphore, FuseMetrics metrics, String commadKey, String groupKey) {
        this.metrics = initMetrics(metrics,commadKey);
        this.breaker = initBreaker(breaker,this.metrics,commadKey);
        this.semaphore=initSemaphore(semaphore,groupKey);
        this.commadKey = commadKey;
        this.groupKey = groupKey;
    }

    protected FuseCommand(String commadKey, String groupKey) {
        this(null,null,null,commadKey,groupKey);
    }

    protected RpcFuture excute(){
        if(breaker.isOpen())//断路器已经打开
            return fallback();
        if(!semaphore.tryAcquire())//信号量已满,无法添加新任务
            return fallback();
        RpcFuture future=run();
        future.setFuseSemaphore(semaphore);
        return future;
    }

    /**
     * 正常执行逻辑
     * 由用户实现
     * @return
     */
    protected abstract RpcFuture run();

    /**
     * 降级逻辑
     * 由用户实现
     * @return
     */
    protected abstract RpcFuture fallback();

    /**
     * 初始化
     * @param metrics
     * @param commadKey
     * @return
     */
    private FuseMetrics initMetrics(FuseMetrics metrics,String commadKey) {
        if(metrics!=null){
            return metrics;
        }else{
            return FuseMetrics.getInstance(commadKey);
        }
    }

    private FuseSemaphore initSemaphore(FuseSemaphore semaphore,String groupKey){
        if(semaphore!=null){
            return semaphore;
        }else{
            return FuseSemaphore.getSemaphore(groupKey);
        }
    }

    private FuseBreaker initBreaker(FuseBreaker breaker,FuseMetrics metrics,String commadKey){
        if(breaker!=null){
            return breaker;
        }else{
            return FuseBreaker.getInstance(commadKey,metrics);
        }
    }

}
