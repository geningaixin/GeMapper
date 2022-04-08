package com.gening.library.gemapper.web.user.enums;


import com.gening.library.gemapper.common.typehandler.enums.GenericEnum;

public enum Sex implements GenericEnum<Integer, String, Sex> {

    MALE(1, "男"),
    FEMALE(2, "女");

    private final int value;
    private final String description;

    Sex(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Sex get() {
        return this;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
