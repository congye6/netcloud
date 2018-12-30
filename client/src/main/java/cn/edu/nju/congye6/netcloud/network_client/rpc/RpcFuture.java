package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
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

    private List<RpcCallBack> callBacks=new ArrayList<>();

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
        countDownLatch.countDown();
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
     * 设置future的结果
     * @param response
     */
    void set(RpcResponse response){
        synchronized (this){//原子性保证response只能设置一次
            if(isDone()){
                LOGGER.warn("rpc future set twice,requestId:"+response.getRequestId());
                return;
            }
            this.response=response;
        }
        countDownLatch.countDown();//实发latch，唤醒所有等待线程
        for(RpcCallBack callBack:callBacks){//异步执行，避免用户操作阻塞
            RpcTaskExecutor.excute(()->callBack.callBack(response.getResponse(),response.getHeaders()));
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

    void setRequestFuture(ChannelFuture channelFuture){
        this.requestFuture=channelFuture;
    }

}
