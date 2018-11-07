package cn.edu.nju.congye6.netcloud.network_client.http;

import cn.edu.nju.congye6.netcloud.service_router.CloudServiceRouter;
import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.*;

/**
 * Created by cong on 2018-10-24.
 */
public class HttpClient {

    private static final Logger LOGGER=Logger.getLogger(HttpClient.class);

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");

    private static final Map<RequestMethod,RequestMethod> REQUEST_METHOD_MAP=new HashMap<>();

    private static final String REQUEST_PARAM_START="{";

    private static final String REQUEST_PARAM_END="}";

    private static final String SOURCE_SERVICE_KEY="cn.edu.nju.congye6.cloudservice.name";

    static {
        REQUEST_METHOD_MAP.put(RequestMethod.GET,RequestMethod.GET);
        REQUEST_METHOD_MAP.put(RequestMethod.DELETE,RequestMethod.POST);
        REQUEST_METHOD_MAP.put(RequestMethod.PUT,RequestMethod.POST);
        REQUEST_METHOD_MAP.put(RequestMethod.POST,RequestMethod.POST);
    }

    private CloudServiceRouter router=CloudServiceRouter.getServiceRouter();

    /**
     * 发送http请求
     * @param returnType
     * @param parameters
     * @param url
     * @return
     */
    public Object send(Class<?> returnType, Object[] parameters, String serviceName, RequestMapping requestMapping) {

        try {
            if(requestMapping==null|| StringUtils.isEmpty(requestMapping.value()))
                throw new Exception("please configure url with @RequestMapping");

            OkHttpClient client = new OkHttpClient();

            String address=router.getAddress(serviceName);

            Request request=buildRequest("http://"+address+requestMapping.value()[0],parameters,requestMapping);
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }
            if(returnType==String.class)
                return response.body().string();
            return JSONObject.parseObject(response.body().string(), returnType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Request buildRequest(String url,Object[] params,RequestMapping requestMapping) throws Exception{
        RequestMethod method=selectMethod(requestMapping.method(),params.length);
        if(method==RequestMethod.GET)
            return builGetRequest(url,params,requestMapping);
        return buildPostRequest(url,params);
    }

    /**
     * 选择使用get或者post
     * @param methods
     * @param paramNums
     * @return
     * @throws Exception
     */
    private RequestMethod selectMethod(RequestMethod[] methods,int paramNums) throws Exception{
        if(methods.length==0){//未配置
            methods=new RequestMethod[]{
                    RequestMethod.POST,RequestMethod.GET
            };
        }

        if(paramNums>1){//参数个数大于1，不能使用post
            for(RequestMethod method:methods){
                if(method==RequestMethod.GET)
                    return RequestMethod.GET;
            }
            throw new Exception("Post 请求只支持一个参数");
        }

        //优先选择Post，否则get
        for(RequestMethod method:methods){
            if(REQUEST_METHOD_MAP.get(method)==RequestMethod.POST)
                return RequestMethod.POST;
        }
        return RequestMethod.GET;
    }


    /**
     * 生成get请求
     * 参数使用{}标注
     * @param originalUrl      url可能存在参数，需要用参数改写url
     * @param requestMapping
     * @return
     */
    private Request builGetRequest(String originalUrl,Object[] params,RequestMapping requestMapping){
        String url=replaceParam(originalUrl, params);
        return commonHeader(new Request.Builder())
                .url(url)
                .build();
    }

    /**
     * 将{}标注的参数替换成值
     * @param originalUrl
     * @param params
     * @return
     */
    private String replaceParam(String originalUrl, Object[] params) {
        StringBuilder builder=new StringBuilder(originalUrl);
        for(Object param:params){
            int start=builder.indexOf(REQUEST_PARAM_START);
            int end=builder.indexOf(REQUEST_PARAM_END);
            if(start==-1||end==-1)
                break;
            builder.replace(start,end+1,param.toString());
        }
        return builder.toString();
    }

    public static void main(String[] args){
        String originalUrl="http://localhost/{ttt}/12/{222}/{33}";
        Object[] params={1,2,"dfk"};
        System.out.println(new HttpClient().replaceParam(originalUrl,params));
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
                .header("Source", PropertyUtil.getProperty(SOURCE_SERVICE_KEY))
                .header("Accept", "application/json");
    }


}
