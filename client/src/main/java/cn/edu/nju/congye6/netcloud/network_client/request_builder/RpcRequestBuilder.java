package cn.edu.nju.congye6.netcloud.network_client.request_builder;

import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcRequest;

/**
 * Created by cong on 2018-11-15.
 */
public class RpcRequestBuilder implements RequestBuilder{

    private RpcRequest request;

    public RpcRequestBuilder(RpcRequest request) {
        this.request = request;
    }

    @Override
    public void addHeader(String key, String value) {
        request.addHeader(key,value);
    }

}
