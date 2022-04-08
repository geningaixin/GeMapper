package com.gening.library.gemapper.common.mapper.exception;

/**
 * @author G
 * @version 1.0
 * @className BeanPropertyException
 * @description 属性获取失败
 * @date 2022/3/18 16:58
 */
public class BeanPropertyException extends RuntimeException {

    private static final long serialVersionUID = 5780250281287561822L;

    public BeanPropertyException() {
        super();
    }

    public BeanPropertyException(String message) {
        super(message);
    }
}
