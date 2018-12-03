package cn.edu.nju.congye6.netcloud.network_client.rpc;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * rpc公用线程池
 * Created by cong on 2018-12-03.
 */
public class RpcTaskExecutor {

    /**
     * 公用线程池
     */
    private static final Executor TASK_EXECUTOR= Executors.newCachedThreadPool();

    static void excute(Runnable runnable){
        TASK_EXECUTOR.execute(runnable);
    }
}
