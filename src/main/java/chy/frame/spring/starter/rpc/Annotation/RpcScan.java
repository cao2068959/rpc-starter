package chy.frame.spring.starter.rpc.Annotation;

import chy.frame.spring.starter.rpc.autoConfigure.FindServiceScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(FindServiceScan.class)
@Documented
public @interface RpcScan {

    String[] basePackage() default {};
}
