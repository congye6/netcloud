package cn.edu.nju.congye6.netcloud.network_client.rpc;

import java.util.Map;

/**
 * rpc回调函数接口
 * Created by cong on 2018-12-02.
 */
public interface RpcCallBack {

    /**
     * 成功获取响应后回调业务端
     * @param json      返回的json字符串，调用端自行解析
     * @param response  响应头，类似于http response header
     */
    public void callBack(String json,Map<String,String> response);

}
