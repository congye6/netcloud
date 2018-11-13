package cn.edu.nju.congye6.netcloud.network_client.rpc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    private ReentrantLock lock=new ReentrantLock();

    /**
     * 当任务未完成时，进入等待队列
     */
    private Condition condition=lock.newCondition();

    /**
     * 响应
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
        lock.lock();
        try {
            if(isDone())
                condition.await();
        }finally {
            lock.unlock();
        }

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
        lock.lock();
        try {
            if(isDone()){
                boolean success=condition.await(timeout,unit);
                if(!success)
                    throw new TimeoutException("获取响应超时");
            }
        }finally {
            lock.unlock();
        }

        return response;
    }

    /**
     * 设置future的结果
     * @param response
     */
    public void set(RpcResponse response){
        lock.lock();
        try {
            this.response=response;
            condition.signalAll();//通知所有等待线程
        }finally {
            lock.unlock();
        }
    }
}
