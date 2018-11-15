package cn.edu.nju.congye6.netcloud.network_client.request_builder;

/**
 * 构造请求的接口
 * Created by cong on 2018-11-15.
 */
public interface RequestBuilder {

    /**
     * 添加请求头
     * @param key
     * @param value
     */
    public void addHeader(String key,String value);

}
