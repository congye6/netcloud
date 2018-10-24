package cn.edu.nju.congye6.netcloud.network_client.http;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Created by cong on 2018-10-24.
 */
public class HttpClient {

    MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");

    /**
     * 发送http请求
     * TODO 未测试
     *
     * @param returnType
     * @param parameters
     * @param url
     * @return
     */
    public Object send(Class<?> returnType, Object[] parameters, String serviceName, RequestMapping requestMapping) {
        requestMapping.method();



        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(requestMapping.value()[0])

                .post()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }
            return JSONObject.parseObject(response.body().string(), returnType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 生成get请求
     * @param originalUrl      url可能存在参数，需要用参数改写url
     * @param requestMapping
     * @return
     */
    private Request builGetRequest(String originalUrl,Object[] params,RequestMapping requestMapping){
        return commonHeader(new Request.Builder())
                .url(originalUrl)
                .build();
    }

    /**
     * 生成post请求
     * @param url
     * @param params 只取第一个参数序列化成json
     * @return
     */
    private Request buildPostRequest(String url,Object[] params){
        //post请求 只有一个对象参数
        String parameterJson = JSONObject.toJSONString(params[0]);
        return commonHeader(new Request.Builder())
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_JSON, parameterJson))
                .build();
    }

    private Request.Builder commonHeader(Request.Builder requestBuilder){
        return requestBuilder.header("User-Agent", "NetCloud")
                .header("Source", "exam")
                .header("Accept", "application/json");
    }


}
