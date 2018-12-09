package cn.edu.nju.congye6.netcloud.network;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc请求
 * Created by cong on 2018-10-24.
 */
public class RpcRequest {

    /**
     * 请求id，标识一次请求
     */
    private String requestId;

    /**
     * 请求方法id，标识一个接口，类似于url
     */
    private String rpcId;

    /**
     * 参数
     */
    private String[] params;

    /**
     * 类似于http请求头
     */
    private Map<String,String> headers=new HashMap<>();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRpcId() {
        return rpcId;
    }

    public void setRpcId(String rpcId) {
        this.rpcId = rpcId;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key,String value){
        if(headers==null)
            headers=new HashMap<>();
        headers.put(key,value);
    }
}
