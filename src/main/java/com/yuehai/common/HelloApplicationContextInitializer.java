package com.yuehai.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * spring容器刷新之前执行的一个回调
 * Created by zhaoyuehai 2019/7/19
 */
public class HelloApplicationContextInitializer implements ApplicationContextInitializer {
    private static Logger logger = LoggerFactory.getLogger(HelloApplicationContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        logger.info("initialize");
    }
}
