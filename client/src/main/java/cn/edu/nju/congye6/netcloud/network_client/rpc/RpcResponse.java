package cn.edu.nju.congye6.netcloud.network_client.rpc;

import java.util.Map;

/**
 * rpc响应
 * Created by cong on 2018-11-11.
 */
public class RpcResponse {

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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
