package cn.edu.nju.congye6.netcloud.fuse;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 统计QPS
 * 将时间分成多个小分区，随着时间推移滑动分区窗口，平滑统计qps
 * Created by cong on 2019-01-03.
 */
public class FuseRollingNumber {

    /**
     * 默认时间间隔为1000ms
     */
    private static final long DEFAULT_TIME_INTERVAL=1000;

    /**
     * 默认分桶个数
     */
    private static final int DEFAULT_BUCKET_SIZE=10;

    /**
     * 总的时间间隔
     */
    private final long timeInterval;

    /**
     * 分桶个数
     */
    private final int bucketSize;

    /**
     * 每个分桶的时间间隔
     */
    private final long bucketInterval;

    /**
     * 分桶环形数组
     */
    private final FuseBucketCircle bucketCircle;

    /**
     * 保护对环形数组的修改
     */
    private final ReentrantLock lock;



    public FuseRollingNumber(long timeInterval, int bucketSize) {
        this.timeInterval = timeInterval;
        this.bucketSize = bucketSize;
        this.bucketInterval=timeInterval/bucketSize;
        this.bucketCircle=new FuseBucketCircle(bucketSize);
        lock=new ReentrantLock();
    }

    public FuseRollingNumber() {
        this(DEFAULT_TIME_INTERVAL,DEFAULT_BUCKET_SIZE);
    }

    /**
     * 获取总数
     * @return
     */
    public int count(FuseEventType eventType){
        int count=0;
        for(FuseQpsBucket bucket:bucketCircle){
            count+=bucket.get(eventType);
        }
        return count;
    }

    /**
     * 递增
     */
    public void increment(FuseEventType eventType){
        FuseQpsBucket currentBucket=getCurrentBucket();
        currentBucket.increment(eventType);
    }

    /**
     * 获取当前的bucket
     * 如果时间超过则加锁创建新bucket
     * 只有一个线程能抢到锁创建，其他线程使用当前最新bucket
     * @return
     */
    private FuseQpsBucket getCurrentBucket(){
        FuseQpsBucket currentBucket=bucketCircle.getHead();
        long currentTime=System.currentTimeMillis();
        if(currentBucket!=null&&currentBucket.getStartTime()+bucketInterval>currentTime){
            //在当前时间范围内
            return currentBucket;
        }

        boolean lockSuccess=lock.tryLock();
        if(lockSuccess){//加锁成功，创建新分桶
            try{
                if(currentBucket==null){//新的rolling number，以当前时间为开始时间
                    FuseQpsBucket newBucket=new FuseQpsBucket(currentTime);
                    bucketCircle.addHead(newBucket);
                    return bucketCircle.getHead();
                }

                if(currentTime-timeInterval>currentBucket.getStartTime()){//当前时间已经超过了整个跨度
                    reset();
                    FuseQpsBucket newBucket=new FuseQpsBucket(currentTime);
                    bucketCircle.addHead(newBucket);
                    return bucketCircle.getHead();
                }

                for(int i=0;i<bucketSize;i++){//当前时间和环形数组头可能隔了几个分桶，依次创建这几个分桶
                    currentBucket=bucketCircle.getHead();
                    FuseQpsBucket newBucket=new FuseQpsBucket(currentBucket.getStartTime()+bucketInterval);
                    bucketCircle.addHead(newBucket);
                    if(newBucket.getStartTime()>currentTime)
                        return newBucket;
                }

            }finally {
                lock.unlock();
            }
        }

        currentBucket=bucketCircle.getHead();//获取锁失败，则使用当前最新的分桶
        if (currentBucket!=null)
            return  currentBucket;
        try {
            Thread.sleep(5);//如果暂时没有分桶,等待获取锁的线程创建新分桶
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getCurrentBucket();
    }

    /**
     * 重置循环数组
     */
    private void reset(){
        bucketCircle.reset();
    }
}
