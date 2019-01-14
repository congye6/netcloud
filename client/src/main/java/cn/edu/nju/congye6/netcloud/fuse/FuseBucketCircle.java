package cn.edu.nju.congye6.netcloud.fuse;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * bucket环形数组
 * Created by cong on 2019-01-04.
 */
public class FuseBucketCircle implements Iterable<FuseQpsBucket>{

    private final int bucketSize;

    private final AtomicReference<CircleState> circleRefrence;

    private class CircleState{
        /**
         * 环形数组头部
         */
        private final int head;

        /**
         * 环形数组尾部
         */
        private final int tail;

        private final AtomicReferenceArray<FuseQpsBucket> buckets;

        private final int size;

        private CircleState(int head, int tail, AtomicReferenceArray<FuseQpsBucket> buckets) {
            this.head = head;
            this.tail = tail;
            this.buckets = buckets;
            if(head==0&&tail==0){
                size=0;
            }else {
                size=(head-tail+bucketSize)%bucketSize;
            }
        }

        private CircleState addHead(FuseQpsBucket qpsBucket){
            buckets.set(head,qpsBucket);
            CircleState newState;
            if(size==bucketSize){
                newState=new CircleState((head+1)%bucketSize,(tail+1)%bucketSize,buckets);
            }else {
                newState=new CircleState((head+1)%bucketSize,tail,buckets);
            }
            return newState;
        }

        private FuseQpsBucket getHead(){
            if(size==0)
                return null;
            return buckets.get((head-1)%bucketSize);
        }

        private FuseQpsBucket[] getArray(){
            FuseQpsBucket[] bucketsCopy=new FuseQpsBucket[size];
            for(int i=0;i<size;i++){
                bucketsCopy[i]=buckets.get((tail+i)%bucketSize);
            }
            return bucketsCopy;
        }

    }



    FuseBucketCircle(int bucketSize){
        this.bucketSize=bucketSize;
        AtomicReferenceArray<FuseQpsBucket> buckets=new AtomicReferenceArray<>(bucketSize+1);
        circleRefrence=new AtomicReference<>(new CircleState(0,0,buckets));
    }

    /**
     * 获取当前bucket
     * @return
     */
    FuseQpsBucket getHead(){
        CircleState state=circleRefrence.get();
        return state.getHead();
    }

    /**
     * 添加bucket
     * 非线程安全
     * @param qpsBucket
     */
    void addHead(FuseQpsBucket qpsBucket){
        CircleState state=circleRefrence.get();
        CircleState newState=state.addHead(qpsBucket);
        circleRefrence.compareAndSet(state,newState);
    }

    /**
     * 重置环形数组
     * 非线程安全
     */
    void reset(){
        AtomicReferenceArray<FuseQpsBucket> buckets=new AtomicReferenceArray<>(bucketSize+1);
        circleRefrence.set(new CircleState(0,0,buckets));
    }


    @Override
    public Iterator<FuseQpsBucket> iterator() {
        return Collections.unmodifiableList(Arrays.asList(circleRefrence.get().getArray())).iterator();
    }


}
