package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import cn.edu.nju.congye6.netcloud.network_client.request_builder.RpcRequestBuilder;
import cn.edu.nju.congye6.netcloud.util.SleepUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使用rpc进行调用
 * Created by cong on 2018-10-24.
 */
public class RpcClient {

    private static final Logger LOGGER= LoggerFactory.getLogger(RpcClient.class);

    private static final long MAX_TIME_OUT=10;

    private static final int RETRY_SLEEP_TIME=1000;


    /**
     * 连接池
     */
    private ChannelPoolManager channelPoolManager = new ChannelPoolManager();


    /**
     * 同步发起调用
     * @param serviceName
     * @param params
     * @param rpcService
     * @param returnType
     * @return
     * @throws Exception
     */
    public Object send(String serviceName, Object[] params, RpcService rpcService,Class<?> returnType) throws Exception{
        RpcFuture future=sendAsync(serviceName,params,rpcService);
        if(RpcFuture.class==returnType)//需要返回future
            return future;
        RpcResponse response=future.get(MAX_TIME_OUT,TimeUnit.SECONDS);
        return JSONObject.parseObject(response.getResponse(),returnType);//根据返回值类型解析响应
    }


    /**
     * 异步发起rpc调用,注意失败重试
     * @param serviceName 服务名称
     * @param params      方法参数
     * @param rpcService  方法注解
     * @return            根据返回值类型解析返回值，如果是RpcFuture将直接返回future
     * @throws Exception
     */
    public RpcFuture sendAsync(String serviceName, Object[] params, RpcService rpcService) throws Exception {
        RpcFuture future=new RpcFuture();
        CallBackBuilder.buildCallBack(rpcService,future,params);

        RpcRequestBuilder requestBuilder=new RpcRequestBuilder();
        RpcRequest request=requestBuilder.build(rpcService,params);

        int retryTimes=rpcService.retryTimes();
        boolean success=false;
        RpcFuture result=null;
        //发起调用,失败进行重试
        while(!success&&retryTimes>=0) {//还没成功并且还有重试次数
            try {
                result = invoke(serviceName, future, request);
                success = true;
            } catch (Exception e) {
                LOGGER.warn("invoke error,retryTimes:" + retryTimes,e);
                if(retryTimes==0)//不可再重试
                    throw e;
                SleepUtil.sleep(RETRY_SLEEP_TIME);//睡眠一定时间再重试
                retryTimes--;//异常重试，次数减一
            }
        }
        return result;
    }

    /**
     * 异步发起rpc调用
     * @param serviceName
     * @param future
     * @param request
     * @return
     * @throws Exception
     */
    private RpcFuture invoke(String serviceName,RpcFuture future, RpcRequest request) throws Exception {
        ChannelPool channelPool=channelPoolManager.getChannelPool(serviceName);
        Channel channel = channelPool.getChannel();//获取channel
        if(channel==null){
            LOGGER.error("连接超时,没有可用channel");
            throw new Exception("连接超时,没有可用channel");
        }
        ResponseHandler responseHandler=channel.pipeline().get(ResponseHandler.class);
        responseHandler.addRpcFuture(request.getRequestId(),future);//设置future，以便异步获取结果
        ChannelFuture channelFuture=channel.writeAndFlush(request);
        future.setRequestFuture(channelFuture);
        return future;
    }


}
