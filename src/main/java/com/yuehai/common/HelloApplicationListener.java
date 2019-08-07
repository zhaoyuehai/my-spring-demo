package com.yuehai.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Spring的监听器 (观察者模式)
 * <p>
 * 通过ApplicationEvent类和ApplicationListener接口，可以实现ApplicationContext事件处理
 * Created by zhaoyuehai 2019/7/19
 */
public class HelloApplicationListener implements ApplicationListener {

    /**
     * 设置泛型类型 过滤你需要的事件来实现你的业务
     * <p>
     * 日志打印的事件有：
     * ApplicationEnvironmentPreparedEvent
     * ApplicationContextInitializedEvent
     * ApplicationPreparedEvent
     * ObjectMapperConfigured
     * ObjectMapperConfigured
     * ContextRefreshedEvent ：ApplicationContext被初始化或刷新时，此处的初始化是指：所有的Bean被成功装载，后处理Bean被检测并激活，所有Singleton Bean 被预实例化，ApplicationContext容器已就绪可用
     * ServletWebServerInitializedEvent
     * ApplicationStartedEvent
     * ApplicationReadyEvent
     * <p>
     * 自定义事件通知： MyXXXEvent extends ApplicationEvent
     * 发布事件：ApplicationContext context.publishEvent(event=new MyXXXEvent(xxx));
     */
//public class HelloApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private static Logger logger = LoggerFactory.getLogger(HelloApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        logger.info("onApplicationEvent: " + event.getClass().getSimpleName());
    }
}
