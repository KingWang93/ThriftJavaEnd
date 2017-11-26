Thrift demo(java)
========

阅读本篇文章的读者需要提前了解一下相关知识，关于thrift的使用，参考http://thrift.apache.org/tutorial/

### 1，本项目中有三种关于thrift的整套调用实现（客户端和服务端）

### 2，采用的thrift文件都是一样的，shared.thrift和tutorial.thrift，这两个文件在thrift官网http://thrift.apache.org/tutorial/java 上可以找到

### 3,三种实现的区别

   以我目前的了解，thrift是一个服务定义的中间件，相当于一个中转站，可以通过他定义一个接口，接口的传入参数和输出结果都有一个格式，至于这个接口可以干嘛，取决于用户去实现这个接口。例如本例中的一个CalculatorHandler，这个就是这个接口的具体实现。thrift本身也可以自己创建端口，通过端口进行数据传输通信，当然也可以用我们常用的http的第三方jar包实现httpserver的创建，这样就就可以实现前后端的通信，同时thrift又实现了js的第三方库，因此可以很好地实现前后端的整套服务调用。对于本项目的demo，还利用了vertx框架实现了创建httpserver，完成前后端的服务调用流程。
   
#### （1）java下的客户端和服务端调用
这部分就是JavaClient.java和JavaServer.java实现的
#### （2）java作服务端（httpcore-4.4.1.jar），js作客户端
这部分对应的代码是Httpd.java，这个代码和官网上提供的Httpd.java的代码唯一区别就是解决了跨域的问题。这部分的代码如下：
```
 response.setHeader("Access-Control-Allow-Origin", "*");
 response.setHeader("Access-Control-Allow-Methods", "POST, GET,OPTIONS");
 response.setHeader("Access-Control-Max-Age", "3600");
 response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 if(method.equals("OPTIONS")) {
    return;
 }
 ```
#### （3）java作服务端（vertx），js作客户端
这部分对应的代码是VertxThriftTest.java

### 注意事项
关于其中涉及到的一部分问题可以参考一下笔者的博客：http://blog.csdn.net/wk1134314305

