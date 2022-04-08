package com.gening.library.gemapper.common.xml;

import com.gening.library.gemapper.common.xml.entity.FieldInfo;
import com.gening.library.gemapper.common.xml.entity.TableInfo;
import org.dom4j.Element;

/**
 * @author G
 * @version 1.0
 * @className IBatisMapperXmlWriter
 * @description Mybatis Mapper写入帮助类
 * @date 2022/3/18 17:45
 */
public class IBatisMapperXmlWriter {

    /**
     * 写入<association><association/>节点
     * @param parent 父级节点
     * @param fieldInfo 字段信息
     */
    public static void writeAssociationElement(Element parent, FieldInfo fieldInfo) {
        TableInfo modelTableMataDate = TableInfo.forClass(fieldInfo.getToModel().modelClz());
        Element element = parent.addElement("association");
        element.addAttribute("property", fieldInfo.getProperty());
        element.addAttribute("javaType", fieldInfo.getJavaType().getName());
        modelTableMataDate.getFieldInfoList().forEach(f -> writeResultElement(element, f));
    }

    /**
     * 写入<collection><collection/>节点
     * @param parent 父级节点
     * @param fieldInfo 字段信息
     */
    public static void writeCollectionElement(Element parent, FieldInfo fieldInfo) {
        TableInfo modelTableMataDate = TableInfo.forClass(fieldInfo.getToList().modelClz());
        Element element = parent.addElement("collection");
        element.addAttribute("property", fieldInfo.getProperty());
        element.addAttribute("javaType", "ArrayList");
        element.addAttribute("ofType", modelTableMataDate.getClzName());
        modelTableMataDate.getFieldInfoList().forEach(f -> writeResultElement(element, f));
    }

    /**
     * 写入<result><result/>节点
     * @param parent 父级节点
     * @param fieldInfo 字段信息
     */
    public static void writeResultElement(Element parent, FieldInfo fieldInfo) {
        Element element = parent.addElement("result");
        element.addAttribute("property", fieldInfo.getProperty());
        element.addAttribute("column", fieldInfo.getColumnName());
        element.addAttribute("javaType", fieldInfo.getJavaType().getName());
    }
}
