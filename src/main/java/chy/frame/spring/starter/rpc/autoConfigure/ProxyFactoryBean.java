package chy.frame.spring.starter.rpc.autoConfigure;

import chy.rpc.annotation.ChyRPCServiceFind;
import chy.rpc.core.ChyRpcApplication;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class ProxyFactoryBean<T> implements FactoryBean<T> {


    private Class<T> rpcInterface;


    @Autowired
    private ChyRpcApplication chyRpcApplication;


    public ProxyFactoryBean(Class<T> rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

    @Override
    public T getObject() throws Exception {
        ChyRPCServiceFind serviceFind = rpcInterface.getAnnotation(ChyRPCServiceFind.class);
        String serviceName = serviceFind.serviceName();
        if(serviceName == null || "".equals(serviceName)){
            serviceName = rpcInterface.getName();
        }
        return chyRpcApplication.getService(serviceName,rpcInterface);
    }

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
