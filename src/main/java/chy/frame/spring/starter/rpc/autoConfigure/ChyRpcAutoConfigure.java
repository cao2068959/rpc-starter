package chy.frame.spring.starter.rpc.autoConfigure;

import chy.frame.spring.starter.rpc.properties.RpcProperties;
import chy.rpc.core.ChyRpcApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(ChyRpcApplication.class)
@EnableConfigurationProperties(RpcProperties.class)
public class ChyRpcAutoConfigure {

    @Autowired
    RpcProperties rpcProperties;

    @Bean
    @ConditionalOnMissingBean(ChyRpcApplication.class)
    public ChyRpcApplication initChyRpcApplication(){
        ChyRpcApplication chyRpcApplication = new ChyRpcApplication(rpcProperties.getZookeepeer());
        chyRpcApplication.setPort(rpcProperties.getPort());
        chyRpcApplication.setIp(rpcProperties.getIp());
        return chyRpcApplication;
    }

    /**
     * 扫描所有打过注解的类,把他注册进rpc服务
     */
    @ConditionalOnBean(ChyRpcApplication.class)
    @Bean
    public ChyRpcRegistService registAllService(){
        return new ChyRpcRegistService();
    }


}
