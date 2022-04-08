package com.gening.library.gemapper.core.util;

import com.gening.library.gemapper.core.provider.ApplicationContextProvider;
import io.vavr.control.Try;
import org.apache.ibatis.io.Resources;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * @author G
 * @version 1.0
 * @className PackageUtils
 * @description 包操作工具类
 * @date 2022/3/18 13:04
 */
public class PackageUtils {

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    /**
     * 获取项目跟目录包名
     *
     * @return {@link String}
     */
    public static String getRootPackage() {
        Map<String, Object> applicationMap = ApplicationContextProvider.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class);
        return applicationMap.keySet().stream()
                .map(applicationMap::get)
                .map(obj -> obj.getClass().getPackage().getName())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未获取到项目跟目录包名"));
    }

    /**
     * 通过多个包名正则匹配扫描class
     *
     * @param packagePatterns 包名正则
     * @return {@link Set<Class>}
     */
    public static Set<Class<?>> scanClassesByPackagePatterns(String packagePatterns) {
        return Stream.of(packagePatterns)
                .map((pps) -> tokenizeToStringArray(pps, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS))
                .flatMap(Arrays::stream)
                .map(PackageUtils::scanClassesByPackage)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * 扫描指定包下所有Class
     *
     * @param packageName 包名
     * @return {@link Set <Class<?>>}
     */
    public static Set<Class<?>> scanClassesByPackage(String packageName) {
        return Stream.of(packageName)
                .map(pn -> Try.of(() -> scanResourcesByPackage(pn)).get())
                .flatMap(Arrays::stream)
                .map(resource -> Try.of(() -> scanClassByResource(resource)).get())
                .collect(Collectors.toSet());
    }

    /**
     * 扫描指定包名下所有Class资源
     *
     * @param packageName 包名
     * @return {@link Resource[]}
     * @throws IOException IO异常
     */
    public static Resource[] scanResourcesByPackage(String packageName) throws IOException {
        return RESOURCE_PATTERN_RESOLVER.getResources(
                ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                        + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class");
    }

    /**
     * 通过资源获取类Class
     *
     * @param resource 资源
     * @return {@link Class}
     * @throws ClassNotFoundException 类不存在异常
     * @throws IOException            IO异常
     */
    public static Class<?> scanClassByResource(Resource resource) throws ClassNotFoundException, IOException {
        ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
        return Resources.classForName(classMetadata.getClassName());
    }
}
