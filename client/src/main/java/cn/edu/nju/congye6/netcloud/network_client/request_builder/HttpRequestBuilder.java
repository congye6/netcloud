package cn.edu.nju.congye6.netcloud.network_client.request_builder;

import com.squareup.okhttp.Request;

/**
 * Created by cong on 2018-11-15.
 */
public class HttpRequestBuilder implements RequestBuilder{

    private Request.Builder request;

    public HttpRequestBuilder(Request.Builder request) {
        this.request = request;
    }

    @Override
    public void addHeader(String key, String value) {
        request.header(key,value);
    }
}
