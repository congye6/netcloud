package cn.edu.nju.congye6.netcloud.network_client.request_builder;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
import cn.edu.nju.congye6.netcloud.network_client.rpc.RpcRequest;
import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
import cn.edu.nju.congye6.netcloud.util.UuidUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cong on 2018-11-15.
 */
public class RpcRequestBuilder implements RequestBuilder{

    private static final String SOURCE_SERVICE_KEY = "cn.edu.nju.congye6.cloudservice.name";

    /**
     * 拦截器执行器
     */
    private RequestInterceptorPipeline pipeline=new RequestInterceptorPipeline();

    private RpcRequest request;

    public RpcRequest build(RpcService rpcService,Object[] params) throws Exception{
        request = new RpcRequest();
        request.setRpcId(UuidUtil.uuid());//requestid 为 uuid

        String rpcId=rpcService.rpcId();
        if (StringUtils.isEmpty(rpcId))
            throw new Exception("rpcId 不能为空");
        request.setRpcId(rpcId);

        RpcContentType contentType = rpcService.contentType();
        String[] rowParams = paramsToString(params, contentType);
        request.setParams(rowParams);

        Map<String, String> header = new HashMap<>();
        header.put("Source", PropertyUtil.getProperty(SOURCE_SERVICE_KEY));
        header.put("ContentType", contentType.toString());
        request.setHeaders(header);

        pipeline.pipeline(this);
        return request;
    }

    @Override
    public void addHeader(String key, String value) {
        request.addHeader(key,value);
    }

    /**
     * 将参数转成字符串
     * @param params
     * @param contentType
     * @return
     * @throws Exception
     */
    private String[] paramsToString(Object[] params, RpcContentType contentType) throws Exception {
        if (params == null || params.length == 0)
            return null;

        if (contentType == RpcContentType.JSON) {//json编码
            if (params.length > 1)
                throw new Exception("json参数个数只能为1");
            return new String[]{JSONObject.toJSONString(params[1])};
        }

        //字符串
        String[] rowParams = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            rowParams[i] = params[i].toString();
        }

        return rowParams;
    }

}
