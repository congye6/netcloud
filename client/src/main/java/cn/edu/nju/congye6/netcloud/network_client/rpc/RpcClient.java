package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
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

    private ChannelPool channelPool = ChannelPool.getInstance();

    public Object send(String serviceName, Object[] params, RpcService rpcService) throws Exception {
        RpcRequest request = new RpcRequest();
        request.setRpcId(UuidUtil.uuid());

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

        String address = serviceRouter.getAddress(serviceName);
        Channel channel = channelPool.getChannel(address);
        channel.writeAndFlush(request);

        ResponseHandler responseHandler;
        RpcFuture future=new RpcFuture();


        return null;
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
