package com.gening.library.gemapper.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author G
 * @version 1.0
 * @className DruidConfig
 * @description Druid配置
 * @date 2022/3/18 16:58
 */
@Configuration
@PropertySource(value = {"classpath:druid-${spring.profiles.active}.yml"}, factory = YmlSourceFactory.class)
@ConfigurationProperties(prefix = "druid.stratview")
public class DruidConfig {

    @Setter
    private String urlMappings;
    @Setter
    private String allow;
    @Setter
    private String deny;
    @Setter
    private String loginUsername;
    @Setter
    private String loginPassword;
    @Setter
    private String resetEnable;
    @Setter
    private String filterUrlPatterns;
    @Setter
    private String filterExclusions;

    /**
     * 初始化 DruidDataSource
     * ConfigurationProperties注解：读取druid.yml中前缀为druid.datasource所有值，并根据获取到的属性走DruidDataSource构造方法，自动装配出DruidDataSource实例
     *
     * @return {@link DruidDataSource}
     */
    @Bean
    @ConfigurationProperties(prefix = "druid.datasource")
    public DruidDataSource getDruidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 配置Druid管理界面的Servlet
     *
     * @return {@link ServletRegistrationBean}
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(), urlMappings);
        // IP白名单 (没有配置或者为空，则允许所有访问)
        registrationBean.addInitParameter("allow", allow);
        // IP黑名单 (存在共同时，deny优先于allow)
        registrationBean.addInitParameter("deny", deny);
        registrationBean.addInitParameter("loginUsername", loginUsername);
        registrationBean.addInitParameter("loginPassword", loginPassword);
        registrationBean.addInitParameter("resetEnable", resetEnable);
        return registrationBean;
    }

    /**
     * 配置Druid的Filter
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatViewFilter() {
        FilterRegistrationBean<WebStatFilter> registrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        registrationBean.addInitParameter("urlPatterns", filterUrlPatterns);
        registrationBean.addInitParameter("exclusions", filterExclusions);
        return registrationBean;
    }
}
