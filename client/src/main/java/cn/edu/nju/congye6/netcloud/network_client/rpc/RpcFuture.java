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

    private ReentrantLock lock=new ReentrantLock();

    private Condition condition=lock.newCondition();

    private RpcResponse response;

    public boolean isDone() {
        return false;
    }

    public RpcResponse get() throws InterruptedException, ExecutionException {
        lock.lock();
        try {
            if(response==null)
                condition.await();
        }finally {
            lock.unlock();
        }

        return response;
    }

    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        lock.lock();
        try {
            if(response==null){
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
