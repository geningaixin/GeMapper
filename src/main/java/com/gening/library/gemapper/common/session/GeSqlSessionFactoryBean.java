package com.gening.library.gemapper.common.session;

import com.alibaba.druid.pool.DruidDataSource;
import com.gening.library.gemapper.common.typehandler.enums.GenericEnum;
import com.gening.library.gemapper.common.typehandler.enums.GenericEnumTypeHandler;
import com.gening.library.gemapper.common.xml.IBatisMapperXmlHelper;
import com.gening.library.gemapper.core.annotation.GeTypeHandler;
import com.gening.library.gemapper.core.annotation.Table;
import com.gening.library.gemapper.core.constant.Constant;
import com.gening.library.gemapper.core.provider.ApplicationContextProvider;
import com.gening.library.gemapper.core.util.AnnotationUtils;
import com.gening.library.gemapper.core.util.BeanUtils;
import com.gening.library.gemapper.core.util.PackageUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className GeSqlSessionFactoryBean
 * @description 自定义SqlSessionFactoryBean，主要实现：
 * 1、初始化时设定JDBC类型
 * 2、XML文件生成
 * 3、注册TypeHandler
 * 4、更新insert方法Options指定主键
 * @date 2022/3/18 17:45
 */
public class GeSqlSessionFactoryBean extends SqlSessionFactoryBean {

    public static String JDBC_TYPE = Constant.DB_TYPE_UNKNOWN;
    public static String PO_PACKAGE_KEYWORD = "model";
    public static String DAO_PACKAGE_KEYWORD = "dao";

    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
        // 设定 JDBC TYPE
        String jdbcDriverClassName = BeanUtils.getValueToEntityByParentFileName(this, "dataSource", DruidDataSource.class).getDriverClassName();
        if (jdbcDriverClassName.contains(Constant.DB_TYPE_MYSQL)) {
            JDBC_TYPE = Constant.DB_TYPE_MYSQL;
        } else if (jdbcDriverClassName.contains(Constant.DB_TYPE_ORACLE)) {
            JDBC_TYPE = Constant.DB_TYPE_ORACLE;
        } else if (jdbcDriverClassName.contains(Constant.DB_TYPE_SQLSERVER)) {
            JDBC_TYPE = Constant.DB_TYPE_SQLSERVER;
        } else if (jdbcDriverClassName.contains(Constant.DB_TYPE_POSTGRESQL)) {
            JDBC_TYPE = Constant.DB_TYPE_POSTGRESQL;
        }
        // 调用父类方法获取SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
        // 为Configuration添加通用枚举类型转换器
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(GenericEnum.class, GenericEnumTypeHandler.class);
        // 扫描项目所有class
        Set<Class<?>> allClasses = PackageUtils.scanClassesByPackage(PackageUtils.getRootPackage());
        // 包含@Table注解则创建并写入Mapper文件
        allClasses.stream().filter(clz -> clz.isAnnotationPresent(Table.class))
                .forEach(IBatisMapperXmlHelper::createMapperXml);
        // 包含@GeTypeHandler注解则加入TypeHandler转换器
        allClasses.stream().filter(clz -> clz.isAnnotationPresent(GeTypeHandler.class))
                .forEach(clz -> sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(BeanUtils.getGeneric(clz), clz));
        // 获取所有Mapper接口，更新Insert方法中Options注解里keyProperty属性填充
        this.getMappers().forEach(this::replaceInsertOptionsValueToMapper);
        return sqlSessionFactory;
    }

    /**
     * 获取所有Mapper接口的Class对象
     *
     * @return {@link Set<Class>}
     */
    private Set<Class<?>> getMappers() {
        return Stream.of(ApplicationContextProvider.getApplicationContext())
                // 通过SpringBoot Application获取MapperScannerConfigurer实例
                .map(application -> application.getBean(MapperScannerConfigurer.class))
                // 反射获取MapperScannerConfigurer中basePackage字段，即mapper文件路径
                .map(mapperScannerConfigurer -> BeanUtils.getValueByFieldName(mapperScannerConfigurer, "basePackage"))
                // 转字符串
                .map(String.class::cast)
                // 扫描路径得到SET
                .map(PackageUtils::scanClassesByPackagePatterns)
                // 转成Set流对象
                .flatMap(Set::stream)
                // 过滤是否为接口
                .filter(Class::isInterface)
                // 组合新结果
                .collect(Collectors.toSet());
    }

    /**
     * 替换Mapper接口下 insertToUseGeneratedKey()和 insertSelectiveToUseGeneratedKey() 方法中 Options注解属性值，将keyProperty动态修改为泛型PO主键
     *
     * @param mapperClass Mapper接口Class
     */
    private void replaceInsertOptionsValueToMapper(final Class<?> mapperClass) {
        final Field keyField = PoUtils.getSimpleKeySqlField(BeanUtils.getGeneric(mapperClass));
        Stream.of(mapperClass)
                .filter(clz -> Objects.nonNull(keyField))
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(method -> "insertToUseGeneratedKey".equals(method.getName()) || "insertSelectiveToUseGeneratedKey".equals(method.getName()))
                .forEach(method -> AnnotationUtils.replaceMethodAnnotationValue(method, Options.class, "keyProperty", keyField.getName()));
    }

    public static void setPoPackageKeyword(String poPackageKeyword) {
        PO_PACKAGE_KEYWORD = poPackageKeyword;
    }

    public static void setDaoPackageKeyword(String daoPackageKeyword) {
        DAO_PACKAGE_KEYWORD = daoPackageKeyword;
    }
}
