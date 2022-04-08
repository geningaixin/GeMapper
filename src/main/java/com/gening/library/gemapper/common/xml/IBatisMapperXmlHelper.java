package com.gening.library.gemapper.common.xml;

import com.gening.library.gemapper.common.mapper.exception.ModelLoadingException;
import com.gening.library.gemapper.common.mapper.exception.ModelResolveException;
import com.gening.library.gemapper.common.session.GeSqlSessionFactoryBean;
import com.gening.library.gemapper.common.xml.entity.FieldInfo;
import com.gening.library.gemapper.common.xml.entity.TableInfo;
import io.vavr.control.Try;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className IBatisMapperXmlHelper
 * @description Mybatis Mapper生成帮助类
 * @date 2022/3/18 17:45
 */
@Slf4j
public class IBatisMapperXmlHelper {

    private static final String LOCAL_PATH_TARGET = "/" + GeSqlSessionFactoryBean.PO_PACKAGE_KEYWORD + "/";
    private static final String LOCAL_PATH_REPLACEMENT = "/" + GeSqlSessionFactoryBean.DAO_PACKAGE_KEYWORD + "/";
    private static final String DOCUMENT_BASE_RESULT_MAP_ELEMENT_NAME = "resultMap";
    private static final String DOCUMENT_BASE_RESULT_MAP_ELEMENT_ID = "BaseResultMap";

    /**
     * 创建PO对应的mapper.xml文件
     *
     * @param poClz 实体列Class
     */
    public static void createMapperXml(final Class<?> poClz) {
        Optional.ofNullable(poClz)
                // 获取XML文件
                .map(IBatisMapperXmlHelper::getMapperXmlFile)
                // 写入XML文件
                .ifPresent(file -> writeMapperXml(poClz, file));
    }

    /**
     * 获取po对应Mapper文件实例并完成创建
     *
     * @param poClz 实体类Class
     * @return {@link File}
     */
    private static File getMapperXmlFile(final Class<?> poClz) {
        return Stream.of(poClz)
                // 获取po对应Mapper文件路径
                .map(IBatisMapperXmlHelper::getMapperXmlFilePath)
                // 创建文件实例
                .map(File::new)
                // 判断文件是否存在，不存在重新创建
                .peek(IBatisMapperXmlHelper::createMapperXmlFile)
                // 判断当前文件是否存在以及类型是否是文件格式
                .filter(file -> file.exists() && file.isFile())
                // 正常则返回，不正常则抛出异常
                .findFirst().orElseThrow(() -> new ModelLoadingException("对应实体类Xml文件不存在"));
    }

    /**
     * 获取po对应Mapper文件路径
     *
     * @param poClz 实体类Class
     * @return {@link String}
     */
    private static String getMapperXmlFilePath(final Class<?> poClz) {
        return Stream.of(IBatisMapperXmlHelper.class)
                // ClassPath绝对路径
                .map(helperClass -> Objects.requireNonNull(helperClass.getResource("/")).getPath())
                // 追加实体类包名+类名
                .map(classpath -> classpath.concat(poClz.getName().replaceAll("\\.", "/")))
                // 将PO路径替换修改成DAO路径，默认为 /po/ -> /dao/
                .map(poPath -> poPath.replaceAll(LOCAL_PATH_TARGET, LOCAL_PATH_REPLACEMENT))
                // 追加Mapper文件后缀，如：UserInfo -> UserInfoMapper.xml
                .map(mapperPath -> mapperPath.concat("Mapper.xml"))
                // 调试输出
                .peek(log::info)
                // 获取结果
                .findFirst()
                // 获取失败抛出异常
                .orElseThrow(() -> new RuntimeException("获取Mapper文件路径失败"));
    }

    /**
     * 文件不存在时，创建Mapper文件
     *
     * @param file mapper文件
     */
    private static void createMapperXmlFile(final File file) {
        Optional.of(file)
                // 文件不存在
                .filter(f -> !f.exists())
                // 创建文件并返回创建成功失败的Boolean值
                .map(f -> Try.of(f::createNewFile).getOrElse(false))
                // 如果文件存在则直接返回True
                .or(() -> Optional.of(true))
                // 过滤失败情况
                .filter(bool -> !bool)
                // 如果当前是创建失败
                .ifPresent((bool) -> log.error("XML文件创建失败，当前文件路径：" + file.getPath()));
    }

    /**
     * XMl文件内容写入
     *
     * @param modelClz 实体类Class
     * @param xmlFile  XML文件对象
     */
    private static void writeMapperXml(final Class<?> modelClz, final File xmlFile) {
        // 根据当前实体类获取表格信息
        TableInfo tableMataDate = TableInfo.forClass(modelClz);
        // 获取构建Document，创建完成RootElement
        Document document = buildDocument(xmlFile, tableMataDate);
        // 判断RootElement节点下是否存在 resultMap -> BaseResultMap，存在则移除
        removeBaseResultMapElement(document);
        // 构建ResultMap节点
        buildResultMap(document, tableMataDate);
        // 写入Document
        writeDocument(xmlFile, document);
    }

    /**
     * 构建XML文件Document对象
     *
     * @param file          XML文件
     * @param tableMataDate 实体类对应表元素信息
     * @return {@link Document}
     */
    private static Document buildDocument(final File file, final TableInfo tableMataDate) {
        // File不存在或不是文件则直接抛出异常
        if (!file.exists() || !file.isFile()) {
            throw new ModelLoadingException("对应实体类Xml文件不存在");
        }
        // 从源文件中读取文档，若源文件中没有文档对象则重新创建并返回
        return Optional.of(file)
                .map(IBatisMapperXmlHelper::getDocumentByRead)
                .orElseGet(() -> createNewDocument(tableMataDate));
    }

    /**
     * 通过获取XML文件Document文档对象，
     *
     * @param file XML文件对象
     * @return {@link Document}
     */
    private static Document getDocumentByRead(final File file) {
        return Stream.of("")
                // 创建SAXReader
                .map(empty ->  new SAXReader())
                // 设置SAXReader
                .peek(reader -> {
                    reader.setValidation(false);
                    reader.setEntityResolver((publicId, systemId) -> new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes())));
                })
                // 读取Document
                .map(reader -> Try.of(() -> reader.read(file)).getOrElse(() -> null))
                // 过滤null的情况，即SAXReader.read失败，如果不过滤，则产生的是Optional(null)，findFirst会报错，过滤后产生的是Optional(Empty)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    /**
     * 创建新文档对象
     *
     * @param tableMataDate 表元素数据
     * @return {@link Document}
     */
    private static Document createNewDocument(final TableInfo tableMataDate) {
        // Document
        Document document = DocumentHelper.createDocument();
        // DOCTYPE
        document.addDocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN\" \"https://mybatis.org/dtd/mybatis-3-mapper.dtd", null);
        // mapper的节点，创建namespace属性
        document.addElement("mapper").addAttribute("namespace", tableMataDate.getNamespace());
        return document;
    }

    /**
     * 移除resultMap节点中，id为BaseResultMap的节点
     *
     * @param document 文档对象
     */
    private static void removeBaseResultMapElement(final Document document) {
        Stream.of(document)
                // 获取resultMap节点
                .map(d -> d.getRootElement().elements(DOCUMENT_BASE_RESULT_MAP_ELEMENT_NAME))
                // 过滤节点集合不为空
                .filter(list -> !CollectionUtils.isEmpty(list))
                // 转List流
                .flatMap(List::stream)
                // 过滤当前节点ID值为BaseResultMap
                .filter(element -> DOCUMENT_BASE_RESULT_MAP_ELEMENT_ID.equals(element.attribute("id").getValue()))
                // 循环进行移除
                .forEach(element -> document.getRootElement().remove(element));
    }

    /**
     * 构建ResultMap节点
     *
     * @param document      文档对象
     * @param tableMataDate 表元素数据
     */
    private static void buildResultMap(final Document document, final TableInfo tableMataDate) {
        // 添加resultMap节点
        Element resultMapElement = document.getRootElement().addElement(DOCUMENT_BASE_RESULT_MAP_ELEMENT_NAME);
        // 设置id
        resultMapElement.addAttribute("id", DOCUMENT_BASE_RESULT_MAP_ELEMENT_ID);
        // 设置type
        resultMapElement.addAttribute("type", tableMataDate.getClzName());

        // 获取表元素对象中字段信息集合并做校验
        List<FieldInfo> fieldInfoList = Optional.of(tableMataDate)
                .map(TableInfo::getFieldInfoList)
                .filter(list -> !CollectionUtils.isEmpty(list))
                .orElseThrow(() -> new ModelResolveException("当前PO类未定义与数据库对应字段"));
        // 1、处理普通节点，这里必须注意1、2、3的写入顺序，否则Mybatis会识别不了而报错
        fieldInfoList.stream()
                .filter(fi -> ObjectUtils.isEmpty(fi.getToList()) && ObjectUtils.isEmpty(fi.getToModel()))
                .forEach(fi -> IBatisMapperXmlWriter.writeResultElement(resultMapElement, fi));
        // 2、处理<association></association>节点
        fieldInfoList.stream()
                .filter(fi -> !ObjectUtils.isEmpty(fi.getToModel()))
                .forEach(fi -> IBatisMapperXmlWriter.writeAssociationElement(resultMapElement, fi));
        // 3、处理<collection></collection>节点
        fieldInfoList.stream()
                .filter(fi -> !ObjectUtils.isEmpty(fi.getToList()) && ObjectUtils.isEmpty(fi.getToModel()))
                .forEach(fi -> IBatisMapperXmlWriter.writeCollectionElement(resultMapElement, fi));
    }

    /**
     * 文档写入
     *
     * @param xmlFile  XML文件对象
     * @param document 文档对象
     */
    private static void writeDocument(final File xmlFile, final Document document) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        try {
            @Cleanup
            Writer out = new FileWriter(xmlFile);
            @Cleanup
            XMLWriter xmlWriter = new XMLWriter(out, format);
            xmlWriter.write(document);
        } catch (IOException e) {
            throw new RuntimeException("XML写入失败，失败路径：" + xmlFile.getPath());
        }
    }
}
