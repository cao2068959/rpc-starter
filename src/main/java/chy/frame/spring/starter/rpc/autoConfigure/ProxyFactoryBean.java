package chy.frame.spring.starter.rpc.autoConfigure;

import chy.rpc.annotation.ChyRPCServiceFind;
import chy.rpc.core.ChyRpcApplication;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProxyFactoryBean<T> implements FactoryBean<T> {


    private Class<T> rpcInterface;


    @Autowired
    private ChyRpcApplication chyRpcApplication;


    public ProxyFactoryBean(Class<T> rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

    /**
     * 用描述文件,生成真正对象的时候,会调用这个方法
     * 调用的时候生成代理对象
     * @return
     * @throws Exception
     */
    @Override
    public T getObject() throws Exception {
        ChyRPCServiceFind serviceFind = rpcInterface.getAnnotation(ChyRPCServiceFind.class);
        String serviceName = serviceFind.serviceName();
        if(serviceName == null || "".equals(serviceName)){
            serviceName = rpcInterface.getName();
        }
        ConcurrentLinkedQueue e;

        return chyRpcApplication.getService(serviceName,rpcInterface);
    }

    /**
     * 假装我的类型还是 原来的接口类型,不是代理对象
     * 这样 自动注入的时候,类型才能匹配上
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return rpcInterface;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<T> getRpcInterface() {
        return rpcInterface;
    }

    public void setRpcInterface(Class<T> rpcInterface) {
        this.rpcInterface = rpcInterface;
    }


    public ChyRpcApplication getChyRpcApplication() {
        return chyRpcApplication;
    }

    public void setChyRpcApplication(ChyRpcApplication chyRpcApplication) {
        this.chyRpcApplication = chyRpcApplication;
    }
}
