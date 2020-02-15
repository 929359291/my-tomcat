package com.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理类的调用处理器
 */
public class ProxyHandler implements InvocationHandler{
    private Object myServlet;
    public ProxyHandler(Object myServlet){
        this.myServlet = myServlet;
    }
    
    //此函数在代理对象调用任何一个方法时都会被调用。
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
    	System.out.println(proxy.getClass().getName());
        System.out.println("====before====");
        Object result = method.invoke(myServlet, args);
        System.out.println("====after====");
        return result;
    }
}