package chy.frame.spring.starter.rpc.autoConfigure;

import chy.rpc.annotation.ChyRPCServiceFind;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public class FindServiceClassPathScanHandle extends ClassPathBeanDefinitionScanner {

    public FindServiceClassPathScanHandle(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        //添加过滤条件
         addIncludeFilter(new AnnotationTypeFilter(ChyRPCServiceFind.class));
        //调用spring的扫描
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if(beanDefinitionHolders.size() != 0){
            //给扫描出来的接口添加上代理对象
            processBeanDefinitions(beanDefinitionHolders);
        }
        return beanDefinitionHolders;
    }


    /**
     * 给扫描出来的接口添加上代理对象
     * @param beanDefinitions
     */
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            //拿到接口的全路径名称
            String beanClassName = definition.getBeanClassName();
            //把接口的全路径放入ProxyFactoryBean 的构造器中,在构造器中会自动转成 class类型
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            //把扫描出来的接口里面改成一个 生成代理类的工程方法,这个类实现了 factoryBean spring容器在实例化的时候会调用 里面的getObject 方法
            definition.setBeanClass(ProxyFactoryBean.class);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

}
