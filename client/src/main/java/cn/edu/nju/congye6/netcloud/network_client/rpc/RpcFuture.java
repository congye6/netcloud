package cn.edu.nju.congye6.netcloud.network_client.rpc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 服务器异步返回响应，使用future接收结果
 * future task不支持传入结果，并且他还需要管理线程
 * Created by cong on 2018-11-12.
 */
public class RpcFuture{

    /**
     * 加锁
     */
    private CountDownLatch countDownLatch=new CountDownLatch(1);

    /**
     * 响应
     * volatile保证设置完成后，其他线程能马上读到
     */
    private volatile RpcResponse response;

    /**
     * 是否已经完成
     * @return
     */
    public boolean isDone() {
        return response!=null;
    }

    /**
     * 获取响应
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public RpcResponse get() throws InterruptedException, ExecutionException {

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
        this.response=response;
        countDownLatch.countDown();//实发latch，唤醒所有等待线程
    }
}
