package cn.edu.nju.congye6.netcloud.fuse;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.cglib.core.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * bucket环形数组
 * Created by cong on 2019-01-04.
 */
public class FuseBucketCircle implements Iterable<FuseQpsBucket>{

    private static final int INVALID_VALUE=-1;

    private final FuseQpsBucket[] buckets;

    private final int bucketSize;

    /**
     * 环形数组头部
     */
    private volatile int head;

    /**
     * 环形数组尾部
     */
    private volatile int tail;

    FuseBucketCircle(int bucketSize){
        this.bucketSize=bucketSize;
        buckets=new FuseQpsBucket[bucketSize+1];
        head=INVALID_VALUE;
        tail=INVALID_VALUE;
    }

    /**
     * 获取当前bucket
     * @return
     */
    FuseQpsBucket getHead(){
        if(tail==INVALID_VALUE)
            return null;
        return buckets[head];
    }

    /**
     * 添加bucket
     * 非线程安全
     * @param qpsBucket
     */
    void addHead(FuseQpsBucket qpsBucket){
        if(tail==INVALID_VALUE){//第一个bucket
            tail=0;
            head=0;
        }else{//移动head和tail
            head=(head+1)%buckets.length;
            if(head-tail==-1||head-tail==bucketSize){//不足bucket size时不用移动
                tail=(tail+1)%buckets.length;
            }
        }

        buckets[head]=qpsBucket;
    }

    /**
     * 重置环形数组
     * 非线程安全
     */
    void reset(){
        tail=INVALID_VALUE;
        head=INVALID_VALUE;
        for(FuseQpsBucket bucket:buckets){//清空数组
            bucket=null;
        }
    }


    @Override
    public Iterator<FuseQpsBucket> iterator() {


        return null;
    }


}
