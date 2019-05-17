package chy.frame.spring.starter.rpc.autoConfigure;

import chy.rpc.annotation.ChyRPCServiceFind;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

public class RpcBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {

        //从自定义注解中拿name
        String name = getNameByServiceFindAnntation(definition,registry);
        if(name != null && !"".equals(name)){
            return name;
        }
        //走原来的方法
        return super.generateBeanName(definition, registry);
    }

    private String getNameByServiceFindAnntation(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanClassName = definition.getBeanClassName();
        try {
            Class<?> aClass = Class.forName(beanClassName);
            ChyRPCServiceFind annotation = aClass.getAnnotation(ChyRPCServiceFind.class);
            if(annotation == null){
                return null;
            }
            return annotation.beanName();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



}
