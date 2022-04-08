package com.gening.library.gemapper.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.gening.library.gemapper.common.session.GeSqlSessionFactoryBean;
import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author G
 * @version 1.0
 * @className MybatisConfig
 * @description Mybatis配置，用于配置：Mybatis SqlSessionFactory、SqlSessionTemplate、数据源事物管理器
 * @date 2022/3/18 16:58
 */
@org.springframework.context.annotation.Configuration
@PropertySource(value = {"classpath:mybatis.yml"}, factory = YmlSourceFactory.class)
@ConfigurationProperties(prefix = "mybatis")
public class MybatisConfig {

    @Resource
    private DruidDataSource druidDataSource;

    @Setter
    public static String poPackageKeyword;

    @Setter
    private String daoPackageKeyword;

    @Setter
    private String transactionAopPointcutExpression;

    /**
     * 设置主数据源的 SessionFactory
     *
     * @return {@link SqlSessionFactory}
     * @throws Exception resolver.getResources异常
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        Optional.ofNullable(poPackageKeyword).ifPresent(GeSqlSessionFactoryBean::setPoPackageKeyword);
        Optional.ofNullable(daoPackageKeyword).ifPresent(GeSqlSessionFactoryBean::setDaoPackageKeyword);

        SqlSessionFactoryBean bean = new GeSqlSessionFactoryBean();
        bean.setDataSource(druidDataSource);
        bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        return bean.getObject();
    }

    /**
     * 设置数据源 Session模板
     *
     * @return {@link SqlSessionTemplate}
     * @throws Exception getSqlSessionFactory异常
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 设置数据源的事务
     *
     * @return {@link TransactionManager}
     */
    @Bean
    public TransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(druidDataSource);
    }

    /**
     * 设置事务拦截器
     *
     * @param dataSourceTransactionManager PlatformTransactionManager
     * @return {@link TransactionInterceptor}
     */
    @Bean
    public TransactionInterceptor txAdvice(TransactionManager dataSourceTransactionManager) {

        // 默认事务
        DefaultTransactionAttribute defaultTransactionAttribute = new DefaultTransactionAttribute();
        defaultTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // 只读事务
        DefaultTransactionAttribute readonlyTransactionAttribute = new DefaultTransactionAttribute();
        readonlyTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        readonlyTransactionAttribute.setReadOnly(true);

        // 为Service方法添加事务
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("add*", defaultTransactionAttribute);
        source.addTransactionalMethod("save*", defaultTransactionAttribute);
        source.addTransactionalMethod("delete*", defaultTransactionAttribute);
        source.addTransactionalMethod("update*", defaultTransactionAttribute);
        source.addTransactionalMethod("exec*", defaultTransactionAttribute);
        source.addTransactionalMethod("set*", defaultTransactionAttribute);
        source.addTransactionalMethod("get*", readonlyTransactionAttribute);
        source.addTransactionalMethod("query*", readonlyTransactionAttribute);
        source.addTransactionalMethod("find*", readonlyTransactionAttribute);
        source.addTransactionalMethod("list*", readonlyTransactionAttribute);
        source.addTransactionalMethod("count*", readonlyTransactionAttribute);
        source.addTransactionalMethod("is*", readonlyTransactionAttribute);
        // 其他方法使用默认事务
        source.addTransactionalMethod("*", defaultTransactionAttribute);

        return new TransactionInterceptor(dataSourceTransactionManager, source);
    }

    /**
     * 设置事务通知
     *
     * @param dataSourceTransactionManager PlatformTransactionManager
     * @return {@link Advisor}
     */
    @Bean
    public Advisor txAdviceAdvisor(@Autowired TransactionManager dataSourceTransactionManager) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(transactionAopPointcutExpression);
        return new DefaultPointcutAdvisor(pointcut, txAdvice(dataSourceTransactionManager));
    }
}
