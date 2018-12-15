package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cong on 2018-12-15.
 */
public class CallBackBuilder {

    private static final Logger LOGGER= LoggerFactory.getLogger(CallBackBuilder.class);

    /**
     * 从参数中读取callback，添加到future中
     * @param annotation
     * @param future
     * @param params
     * @return
     */
    static Object[] buildCallBack(RpcService annotation,RpcFuture future,Object[] params){
        boolean hasCallBack=annotation.hasCallBack();
        List<RpcCallBack> callBacks=new ArrayList<>();
        if(hasCallBack){//有回调函数
            try {
                callBacks=(List<RpcCallBack>)params[params.length-1];
                params= Arrays.copyOf(params,params.length-1);
            }catch (Exception e){
                LOGGER.warn("callback添加异常");
            }
        }

        for(RpcCallBack callBack:callBacks){
            future.addCallBack(callBack);
        }

        return params;
    }


}
