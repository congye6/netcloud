package cn.edu.nju.congye6.netcloud.network_client.rpc;

import cn.edu.nju.congye6.netcloud.annotation.RpcService;

/**
 * Created by cong on 2018-12-31.
 */
public class RpcClientParam {

    private String serviceName;

    private Object[] args;

    private RpcService annotation;

    private Class<?> returnType;

    public RpcClientParam(String serviceName, Object[] args, RpcService annotation, Class<?> returnType) {
        this.serviceName = serviceName;
        this.args = args;
        this.annotation = annotation;
        this.returnType = returnType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public RpcService getAnnotation() {
        return annotation;
    }

    public void setAnnotation(RpcService annotation) {
        this.annotation = annotation;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
