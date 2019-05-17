package chy.frame.spring.starter.rpc.autoConfigure;

import chy.rpc.annotation.ChyRPCRegist;
import chy.rpc.core.ChyRpcApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

public class ChyRpcRegistService implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ChyRpcApplication chyRpcApplication;

    /**
     * 扫描自定义的注解,并注册到RPC容器中
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //拿到spring容器
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        //拿到所有打了注解 ChyRPCRegist 的bean
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ChyRPCRegist.class);

        boolean isStart = false;

        for (String serviceName : beansWithAnnotation.keySet()) {

            Object serviceObject = beansWithAnnotation.get(serviceName);
            Class<?> serviceImpClass = serviceObject.getClass();
            //拿到注解
            ChyRPCRegist chyRPCRegist = serviceImpClass.getAnnotation(ChyRPCRegist.class);

            String registName = chyRPCRegist.name();
            //如果在注解中没指定名称,就用 接口的名称,
            if("".equals(registName)){
                Class<?>[] interfaces = serviceImpClass.getInterfaces();
                if(interfaces!=null && interfaces.length >0){
                    registName = interfaces[0].getName();
                }
            }

            if(registName == null || "".equals(registName)){
                System.err.println(serviceObject.getClass().getName()+ " ---> 注册失败");
                continue;
            }
            try {
                chyRpcApplication.register(registName,serviceObject);
                isStart = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if(isStart){
            System.out.println("RPC 服务提供者 开启 ---- > 端口 "+ chyRpcApplication.getPort());
        }

    }


}
