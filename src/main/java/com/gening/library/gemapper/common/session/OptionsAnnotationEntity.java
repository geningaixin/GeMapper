package com.gening.library.gemapper.common.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

/**
 * @author G
 * @version 1.0
 * @className OptionsAnnotationEntity
 * @description TODO
 * @date 2022/3/31 10:59
 */
@Getter
@Setter
@AllArgsConstructor
public class OptionsAnnotationEntity {

    private InvocationHandler handler;

    private Field memberValuesField;
}
