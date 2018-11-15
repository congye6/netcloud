package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
import cn.edu.nju.congye6.netcloud.network_client.request_builder.RequestInterceptorPipeline;
import cn.edu.nju.congye6.netcloud.network_client.request_builder.RpcRequestBuilder;
import cn.edu.nju.congye6.netcloud.service_router.CloudServiceRouter;
import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
import cn.edu.nju.congye6.netcloud.util.UuidUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 使用rpc进行调用
 * Created by cong on 2018-10-24.
 */
public class RpcClient {

    private static final String SOURCE_SERVICE_KEY = "cn.edu.nju.congye6.cloudservice.name";

    /**
     * 获取服务地址
     */
    private CloudServiceRouter serviceRouter = CloudServiceRouter.getServiceRouter();

    /**
     * 连接池
     */
    private ChannelPool channelPool = ChannelPool.getInstance();

    /**
     * 拦截器执行器
     */
    private RequestInterceptorPipeline pipeline=new RequestInterceptorPipeline();

    public Object send(String serviceName, Object[] params, RpcService rpcService) throws Exception {
        RpcRequest request = new RpcRequest();
        request.setRpcId(UuidUtil.uuid());//requestid 为 uuid

        String rpcId = rpcService.rpcId();
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

        pipeline.pipeline(new RpcRequestBuilder(request));

        //发起调用
        String address = serviceRouter.getAddress(serviceName);
        Channel channel = channelPool.getChannel(address);//获取channel
        ResponseHandler responseHandler=channel.pipeline().get(ResponseHandler.class);
        RpcFuture future=new RpcFuture();
        responseHandler.addRpcFuture(request.getRequestId(),future);//设置future，以便异步获取结果
        channel.writeAndFlush(request);
        //TODO 异步获取future
        return future.get();
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
