package cn.edu.nju.congye6.netcloud.network_client.rpc;

import java.util.concurrent.*;

/**
 * rpc公用线程池
 * Created by cong on 2018-12-03.
 */
public class RpcTaskExecutor {

    /**
     * 公用线程池
     */
    private static final Executor TASK_EXECUTOR= new ThreadPoolExecutor(20,100,60, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(300));

    static void excute(Runnable runnable){
        TASK_EXECUTOR.execute(runnable);
    }
}
