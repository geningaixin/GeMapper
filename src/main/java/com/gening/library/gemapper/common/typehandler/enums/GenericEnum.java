package com.gening.library.gemapper.common.typehandler.enums;

/**
 * @author G
 * @version 1.0
 * @className GeServiceImpl
 * @description 通用Service实现类
 * @date 2022/3/18 17:45
 * @param <K> KEY
 * @param <V> VALUE
 * @param <C> 自身对象
 */
public interface GenericEnum<K, V, C extends Enum<?>> {
    /**
     * 返回枚举对象
     *
     * @return {@link C}
     */
    C get();

    /**
     * 返回枚举项的 key
     *
     * @return {@link K}
     */
    K getValue();

    /**
     * 返回枚举项的 value
     *
     * @return {@link V}
     */
    V getDescription();
}
