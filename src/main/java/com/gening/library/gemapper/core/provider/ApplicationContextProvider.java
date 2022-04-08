package com.gening.library.gemapper.core.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author G
 * @version 1.0
 * @className ApplicationContextProvider
 * @description SpringBoot Application拓展类，现用于获取Application实例
 * @date 2022/3/18 16:58
 */
@Component
@Slf4j
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 注入 SpringBoot ApplicationContext
     *
     * @param applicationContext SpringBoot ApplicationContext
     * @throws BeansException Beans加载异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    /**
     * 获取 SpringBoot ApplicationContext
     * @return {@link ApplicationContext}
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
