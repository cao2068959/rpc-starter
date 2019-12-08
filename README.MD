## 前言

这是一个什么项目?
> 这是一个 chyRPC 框架的 spring-boot-starter , 为了在spring-boot项目更快更方便使用我的轮子

为什么造这个轮子? 
   
>前面都花时间造了一个RPC轮子,也不差这点时间再封装一个 spring-boot-starter,然而这个轮子花的时间比前面那个还长```



## 准备工作
JDK 8

一个可以访问到的 zookeeper  

这个项目基于 [chyrpc](https://github.com/cao2068959/chyrpc20 "") ,需要先去在maven中安装了这个项目才能使用该starter

 
## 快速体验


### 安装项目

maven中安装了 [chyrpc](https://github.com/cao2068959/chyrpc20 "") 后, 下载本仓库,执行 `maven install` 后 引用项目坐标
```xml
    <dependency>
        <groupId>chy.frame.spring.starter</groupId>
        <artifactId>chyrpc-spring-boot-starter</artifactId>
        <version>1.0</version>
    </dependency>
```

### 配置属性
在 `application.yaml` 文件中配置一些参数
```yaml

chyrpc:
  #提供者 需填写 默认 10086端口
  port: 10099
  #提供者 需填写 默认 127.0.0.1
  ip: "192.168.2.11"
  #消费者/提供者 需填 默认 "127.0.0.1:2181"
  zookeepeer: "127.0.0.1:2181"

```

### 提供者
使用 注解 `@ChyRPCRegist` 来指定要提供的服务 注解中的参数 `name` 代表这个服务的名称

注意: 这里需要定义一个 共有的接口, 让提供者去实现,而消费者使这个接口去调用 ,下面实例中 定义的接口为 `ServiceApi`

```java
@Service
@ChyRPCRegist(name = "service01")
public class MyService implements ServiceApi {

    public String test(){
        return "调用了 8081的服务哦";
    }

    @Override
    public User getUser(Query query) {
        User user = new User();
        user.setDate(new Date());
        user.setName(query.getName());
        user.setAge(query.getAge());
        return user;
    }

}
```

### 消费者

1. 需要给消费者去指定需要消费的接口的指定路径,也就是上面实例中 `ServiceApi` 接口所在的包路径
```yaml
@SpringBootApplication
@RpcScan(basePackage = {"消费接口所在的路径"})
public class Demo4Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Demo4Application.class, args);

    }

}
```

使用 注解 `@ChyRPCServiceFind` 来指定要消费的服务,参数 `serviceName` 为指定要消费的服务名称
(可省略,但是对应的 `@ChyRPCRegist` 中的 `name` 属性也必须省略)
```java
@ChyRPCServiceFind(serviceName = "service01")
public interface ServiceApi {
    public String test();

    public User getUser(Query query);

}

```

然后就能愉快的使用了

还是和普通的 bean 对象一样 使用 `@Autowired` 注解来注入对象后使用

```java

@RestController
public class Mycontroller {

    @Autowired
    private ServiceApi serviceApi;


    @GetMapping("/test3")
    public String test3(){
        return  serviceApi.test();
    }

    @GetMapping("/test4")
    public User test4(){
        return  serviceApi.getUser(new Query("小红",22));
    }

}

```







