package chy.frame.spring.starter.rpc;

import chy.frame.spring.starter.rpc.autoConfigure.ChyRpcAutoConfigure;
import chy.rpc.core.ChyRpcApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

@SpringBootApplication

public class RpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcApplication.class, args);
    }

}
