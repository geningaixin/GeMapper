package com.gening.library.gemapper.web.user.enums;

import com.gening.library.gemapper.common.typehandler.enums.GenericEnum;

public enum State implements GenericEnum<Integer, String, State> {

    NORMAL(0, "正常"),
    DELETE(9, "删除");

    private final int value;
    private final String description;

    State(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public State get() {
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
