package cn.edu.nju.congye6.netcloud.service_invoker;

import cn.edu.nju.congye6.netcloud.util.PropertyUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 执行调用业务逻辑的线程池
 * 线程数量由配置文件指定
 * Created by cong on 2018-12-17.
 */
public class InvokerThreadPool {

    private static final int DEFAULT_THREAD_NUM=30;

    private static InvokerThreadPool instance;

    private Executor threadPool;

    private InvokerThreadPool(){
        initThreadPool();
    }



    static InvokerThreadPool getInstance(){
        if(instance==null){
            synchronized(InvokerThreadPool.class) {
                if(instance==null)
                    instance=new InvokerThreadPool();
            }
        }
        return instance;
    }


    /**
     * 执行任务
     * @param runnable
     */
    void execute(Runnable runnable){
        threadPool.execute(runnable);
    }

    /**
     * 初始化线程池
     */
    private void initThreadPool(){
        int threadNum=getThreadNum();
        threadPool=Executors.newFixedThreadPool(threadNum);
    }

    /**
     * 从配置文件读取线程数
     * @return
     */
    private int getThreadNum(){
        int threadNum;
        try{
            threadNum=Integer.parseInt(PropertyUtil.getProperty("cn.edu.nju.congye6.invoker.thread"));
        }catch (Exception e){
            threadNum=DEFAULT_THREAD_NUM;
        }
        return threadNum;
    }

}
