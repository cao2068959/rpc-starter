package chy.frame.spring.starter.rpc.autoConfigure;

import chy.frame.spring.starter.rpc.Annotation.RpcScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.Set;

public class FindServiceScan implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {


    ResourceLoader resourceLoader;



    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcScan.class.getName()));

        String[] basePackages = annoAttrs.getStringArray("basePackage");
        //没有设置 扫描路径,就扫描对应
        if(basePackages.length == 0){
            basePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }


        FindServiceClassPathScanHandle scanHandle = new FindServiceClassPathScanHandle(beanDefinitionRegistry,false);

        if(resourceLoader != null){
            scanHandle.setResourceLoader(resourceLoader);
        }

        scanHandle.setBeanNameGenerator(new RpcBeanNameGenerator());
        //扫描指定路径下的接口
        Set<BeanDefinitionHolder> beanDefinitionHolders = scanHandle.doScan(basePackages);
    }
}
