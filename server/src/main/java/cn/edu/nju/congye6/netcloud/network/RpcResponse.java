package cn.edu.nju.congye6.netcloud.network;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * rpc响应
 * Created by cong on 2018-11-11.
 */
public class RpcResponse {

    private boolean success;

    /**
     * 标记响应对应的请求
     */
    private String requestId;

    /**
     * 响应参数，json格式
     */
    private String response;

    /**
     * 响应头
     */
    private Map<String,String> headers;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public <T> T getResponse(Class<T> type){
        return JSONObject.parseObject(response,type);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
