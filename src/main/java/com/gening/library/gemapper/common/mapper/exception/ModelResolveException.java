package com.gening.library.gemapper.common.mapper.exception;

/**
 * @author G
 * @version 1.0
 * @className ModelResolveException
 * @description 实体类解析失败
 * @date 2022/3/18 16:58
 */
public class ModelResolveException extends RuntimeException {

    private static final long serialVersionUID = -8815730579011123366L;

    public ModelResolveException() {
        super();
    }

    public ModelResolveException(String message) {
        super(message);
    }
}
