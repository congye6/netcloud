package cn.edu.nju.congye6.netcloud.service_register;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by cong on 2018-10-23.
 */
public class CloudServiceHandler implements InvocationHandler{
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy working");
        return null;
    }
}
