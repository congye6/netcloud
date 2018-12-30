package cn.edu.nju.congye6.netcloud.fuse;

import java.util.*;
import java.util.concurrent.*;

/**
 * 用于隔离的线程池
 * Created by cong on 2018-12-22.
 */
public class FuseThreadPool {



    private static final Map<String,FuseThreadPool> INSTANCE_MAP=new ConcurrentHashMap<>();

    /**
     * 线程池
     * TODO 参数配置化
     */
    private static final ExecutorService THREAD_POOL=new ThreadPoolExecutor(20,100,100, TimeUnit.SECONDS,new LinkedBlockingQueue<>(200));

    public void excute(Runnable runnable){
        THREAD_POOL.execute(runnable);
    }

    public Object call(Callable callable){
        Future future=THREAD_POOL.submit(callable);
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断任务队列是否已满
     * TODO
     * @return
     */
    public boolean isQueueNotAvailable(){
        return true;
    }

    /**
     * 根据groupKey获取线程池实例
     * 不存在则新创建一个
     * @param groupKey
     * @return
     */
    static FuseThreadPool getInstance(String groupKey){
        FuseThreadPool instance=INSTANCE_MAP.putIfAbsent(groupKey,new FuseThreadPool());
        if(instance==null){//instance不存在，说明刚创建的对象加入了map
            return INSTANCE_MAP.get(groupKey);
        }else{//instance已经存在
            return instance;
        }
    }

}
