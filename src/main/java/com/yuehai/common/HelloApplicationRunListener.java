package com.yuehai.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * SpringApplicationRunListener接口的作用主要就是在Spring Boot启动初始化的过程中
 * 可以通过SpringApplicationRunListener接口回调
 * 来让用户在启动的各个流程中可以加入自己的逻辑。
 * <p>
 * Spring Boot启动过程的关键事件（按照触发顺序）包括：
 * 1.开始启动
 * 2.Environment构建完成
 * 3.ApplicationContext构建完成
 * 4.ApplicationContext完成加载
 * 5.ApplicationContext完成刷新并启动
 * 6.启动完成
 * 7.启动失败
 * <p>
 * Created by zhaoyuehai 2019/7/19
 */
public class HelloApplicationRunListener implements SpringApplicationRunListener {
    private static Logger logger = LoggerFactory.getLogger(HelloApplicationRunListener.class);
    private final SpringApplication application;

    /**
     * 实现自定义SpringApplicationRunListener需要实现SpringApplicationRunListener接口
     * 并且必须提供一个包含参数（SpringApplication application, String[] args）的构造方法
     */
    public HelloApplicationRunListener(SpringApplication application, String[] args) {
        this.application = application;
    }

    //在run()方法开始执行时，该方法就立即被调用，可用于在初始化最早期时做一些工作
    @Override
    public void starting() {
        logger.info("starting");

    }

    //当environment构建完成，ApplicationContext创建之前，该方法被调用
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        logger.info("environmentPrepared");
    }

    //当ApplicationContext构建完成时，该方法被调用
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        logger.info("contextPrepared");
    }

    //在ApplicationContext完成加载，但没有被刷新前，该方法被调用
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        logger.info("contextLoaded");
    }

    //在ApplicationContext刷新并启动后，CommandLineRunners和ApplicationRunner未被调用前，该方法被调用
    @Override
    public void started(ConfigurableApplicationContext context) {
        logger.info("started");
    }

    //在run()方法执行完成前该方法被调用
    @Override
    public void running(ConfigurableApplicationContext context) {
        logger.info("running");
    }

    //当应用运行出错时该方法被调用
    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        logger.info("failed");
    }
}
