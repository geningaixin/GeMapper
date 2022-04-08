package com.gening.library.gemapper.common.mapper.exception;

/**
 * @author G
 * @version 1.0
 * @className ModelLoadingException
 * @description 实体类加载失败
 * @date 2022/3/18 16:58
 */
public class ModelLoadingException extends RuntimeException {

    private static final long serialVersionUID = 8779287100880399952L;

    public ModelLoadingException() {
        super();
    }

    public ModelLoadingException(String message) {
        super(message);
    }
}
