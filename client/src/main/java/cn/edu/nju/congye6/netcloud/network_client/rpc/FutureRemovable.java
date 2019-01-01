package cn.edu.nju.congye6.netcloud.network_client.rpc;

/**
 * 从future map中删除future的接口
 * Created by cong on 2019-01-01.
 */
public interface FutureRemovable {

    public void removeFuture(String requestId);

}
