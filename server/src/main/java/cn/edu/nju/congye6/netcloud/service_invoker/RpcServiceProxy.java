package cn.edu.nju.congye6.netcloud.service_invoker;

import cn.edu.nju.congye6.netcloud.enumeration.RpcContentType;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.*;

/**
 * 一个可以调用的rpcService
 * 默认为单例模式，target对象不变
 * Created by cong on 2018-12-05.
 */
public class RpcServiceProxy{

    /**
     * 要调用的方法
     */
    private Method method;

    /**
     * 调用的对象
     */
    private Object target;

    /**
     * 方法参数的类型
     */
    private Class<?>[] paramTypes;

    public RpcServiceProxy(Method method, Object target) {
        this.method = method;
        this.target = target;
        paramTypes=method.getParameterTypes();
    }

    /**
     * 发起调用
     * @param params 原始的调用参数，由本方法解析
     * @return 如果方法没有返回值则返回null
     * @exception IllegalArgumentException  如果参数个数或类型不正确
     * @exception InvocationTargetException 如果调用的方法抛出异常
     */
    public Object invoke(String[] params, RpcContentType contentType) throws InvocationTargetException, IllegalAccessException {
        Object[] actualParams=deserialize(params,paramTypes,contentType);
        return method.invoke(target,actualParams);
    }


    /**
     * 解析参数
     * @param params
     * @param paramTypes
     * @param contentType
     * @return
     */
    private Object[] deserialize(String[] params, Class<?>[] paramTypes,RpcContentType contentType){
        Object[] result=new Object[params.length];
        if(paramTypes.length!=params.length)
            throw new IllegalArgumentException("参数个数不正确,应传参数:"+paramTypes.length+",实际:"+params.length);
        if(contentType==RpcContentType.JSON){//json格式
            if(params.length!=1)
                throw new IllegalArgumentException("json格式参数只能有1个");
            result[0]= JSONObject.parseObject(params[0],paramTypes[0]);
        }else{//基本类型
            for(int i=0;i<params.length;i++){
                result[i]=deserialize(paramTypes[i],params[i]);
            }
        }

        return result;
    }


    /**
     * 解析基本类型
     * @param clazz
     * @param value
     * @return
     */
    private Object deserialize(Class<?> clazz,String value){
        if(clazz==String.class)
            return value;
        if(clazz==Integer.class)
            return Integer.parseInt(value);
        if(clazz==Double.class)
            return Double.parseDouble(value);
        if(clazz==Boolean.class)
            return Boolean.parseBoolean(value);
        if(clazz==Float.class)
            return Float.parseFloat(value);
        if(clazz==Long.class)
            return Long.parseLong(value);
        if(clazz==Character.class)
            return value.charAt(0);
        if(clazz==Byte.class)
            return Byte.parseByte(value);

        throw new IllegalArgumentException("参数不正确,类型:"+clazz.getName()+" 值:"+value);
    }

}
