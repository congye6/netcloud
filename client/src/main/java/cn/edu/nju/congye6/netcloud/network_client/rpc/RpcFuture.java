package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.fuse.FuseMetrics;
import cn.edu.nju.congye6.netcloud.fuse.FuseSemaphore;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 服务器异步返回响应，使用future接收结果
 * future task不支持传入结果，并且他还需要管理线程
 * Created by cong on 2018-11-12.
 */
public class RpcFuture{

    private static final Logger LOGGER= LoggerFactory.getLogger(RpcFuture.class);

    /**
     * 加锁
     */
    private CountDownLatch countDownLatch=new CountDownLatch(1);

    /**
     * 限流信号量,熔断器用于统计并发数
     */
    private FuseSemaphore fuseSemaphore;

    /**
     * 统计访问成功或失败的次数
     */
    private FuseMetrics metrics;

    private List<RpcCallBack> callBacks=new ArrayList<>();

    /**
     * 请求的id
     */
    private String requestId;

    /**
     * 用于成功时删除自己
     */
    private FutureRemovable remover;

    /**
     * 响应
     * volatile保证设置完成后，其他线程能马上读到
     */
    private volatile RpcResponse response;

    /**
     * 发送消息的future
     */
    private ChannelFuture requestFuture;



    /**
     * 是否取消
     */
    private volatile boolean isCancel=false;

    /**
     * 成功或者失败
     */
    private volatile boolean isSuccess=false;

    /**
     * 是否已经完成
     * @return
     */
    public boolean isDone() {
        return response!=null;
    }

    public boolean isCancelled(){
        return requestFuture.isCancelled()||isCancel;
    }

    public void cancel(){
        isCancel=true;
        release();
    }

    /**
     * 获取响应
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public RpcResponse get() throws InterruptedException, ExecutionException {
        if(isCancelled())
            return null;
        if(!isDone())
            countDownLatch.await();

        return response;
    }

    /**
     * 获取响应，超时则抛出异常
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if(isCancelled())
            return null;
        if(!isDone()){
            boolean success=countDownLatch.await(timeout,unit);
            if(!success)
                throw new TimeoutException("获取响应超时");
        }
        return response;
    }

    /**
     * 访问成功
     * 设置future的结果
     * @param response
     */
    void success(RpcResponse response){
        boolean success=setResult(response);
        if(!success){
            LOGGER.warn("rpc future success fail since has done,requestId:"+response.getRequestId());
            return;
        }
        metrics.success();
        isSuccess=true;
    }

    /**
     * 访问失败
     * 设置future的降级处理的结果
     * @param response
     */
    public void fallback(RpcResponse response){
        boolean success=setResult(response);
        if(!success){
            LOGGER.warn("rpc future fallback fail since has done,requestId:"+response.getRequestId());
        }
    }

    /**
     * 设置future的结果
     * 只能设置一次，设置后释放资源
     * @param response
     * @return
     */
    private boolean setResult(RpcResponse response){
        synchronized (this){//原子性保证response只能设置一次
            if(isDone()||isCancelled()){
                return false;
            }
            this.response=response;
        }
        release();
        for(RpcCallBack callBack:callBacks){//异步执行，避免用户操作阻塞
            RpcTaskExecutor.excute(()->callBack.callBack(response.getResponse(),response.getHeaders()));
        }
        return true;
    }



    /**
     * 成功或取消之后释放latch和信号量,删除自己在map中的映射
     */
    private void release(){
        countDownLatch.countDown();
        if(fuseSemaphore!=null){
            fuseSemaphore.release();
        }

        if(remover!=null){//删除映射
            remover.removeFuture(requestId);
        }
    }


    /**
     * 添加回调函数
     * @param callBack
     */
    public synchronized void addCallBack(RpcCallBack callBack){
        if(isDone()){//已经完成，直接执行
            RpcTaskExecutor.excute(()->callBack.callBack(response.getResponse(),response.getHeaders()));
            return;
        }

        callBacks.add(callBack);
    }

    /**
     * 设置限流信号量
     * 如果已经完成，信号量直接减一
     * @param fuseSemaphore
     */
    public synchronized void setFuseMetrics(FuseSemaphore fuseSemaphore,FuseMetrics fuseMetrics){
        if(this.fuseSemaphore!=null)
            return;
        this.fuseSemaphore=fuseSemaphore;
        this.metrics=fuseMetrics;
        if(isDone()||isCancelled()){
            fuseSemaphore.release();
            if(isSuccess)
                metrics.success();
        }

    }

    void setRequestFuture(ChannelFuture channelFuture){
        this.requestFuture=channelFuture;
    }

    void setRemover(String requestId,FutureRemovable remover){
        this.requestId=requestId;
        this.remover=remover;
    }
}
