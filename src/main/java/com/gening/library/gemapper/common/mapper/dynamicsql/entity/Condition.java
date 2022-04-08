package com.gening.library.gemapper.common.mapper.dynamicsql.entity;

import com.gening.library.gemapper.common.mapper.dynamicsql.enums.ConditionMode;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.ConditionType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author G
 * @version 1.0
 * @className Condition
 * @description WHERE条件对象
 * @date 2022/3/18 16:58
 */
@Getter
public class Condition {

    private Class<?> modelClass;

    private String property;

    private ConditionMode mode;

    private ConditionType conditionType;

    private Class<?> targetModelClass;

    private String targetProperty;

    private Object value;

    private List<Condition> orGroups;

    private Condition() {
    }

    public static Condition createOrGroups() {
        Condition condition = new Condition();
        condition.orGroups = new ArrayList<>();
        return condition;
    }

    public Condition addOrContent(Condition condition) {
        this.orGroups.add(condition);
        return this;
    }

    public static Condition createIsNull(Class<?> modelClass, String property) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_SELF;
        condition.conditionType = ConditionType.IS_NULL;
        return condition;
    }

    public static Condition createNotNull(Class<?> modelClass, String property) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_SELF;
        condition.conditionType = ConditionType.NOT_NULL;
        return condition;
    }

    public static Condition createIn(Class<?> modelClass, String property, Object[] value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.IN;
        condition.value = value;
        return condition;
    }

    public static Condition createNotIn(Class<?> modelClass, String property, Object[] value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.NOT_IN;
        condition.value = value;
        return condition;
    }

    public static Condition createGt(Class<?> modelClass, String property, Class<?> targetModelClass, String targetProperty) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_PROPERTY;
        condition.conditionType = ConditionType.GT;
        condition.targetModelClass = targetModelClass;
        condition.targetProperty = targetProperty;
        return condition;
    }

    public static Condition createGt(Class<?> modelClass, String property, Object value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.GT;
        condition.value = value;
        return condition;
    }

    public static Condition createGte(Class<?> modelClass, String property, Class<?> targetModelClass, String targetProperty) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_PROPERTY;
        condition.conditionType = ConditionType.GTE;
        condition.targetModelClass = targetModelClass;
        condition.targetProperty = targetProperty;
        return condition;
    }

    public static Condition createGte(Class<?> modelClass, String property, Object value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.GTE;
        condition.value = value;
        return condition;
    }

    public static Condition createLt(Class<?> modelClass, String property, Class<?> targetModelClass, String targetProperty) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_PROPERTY;
        condition.conditionType = ConditionType.LT;
        condition.targetModelClass = targetModelClass;
        condition.targetProperty = targetProperty;
        return condition;
    }

    public static Condition createLt(Class<?> modelClass, String property, Object value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.LT;
        condition.value = value;
        return condition;
    }

    public static Condition createLte(Class<?> modelClass, String property, Class<?> targetModelClass, String targetProperty) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_PROPERTY;
        condition.conditionType = ConditionType.LTE;
        condition.targetModelClass = targetModelClass;
        condition.targetProperty = targetProperty;
        return condition;
    }

    public static Condition createLte(Class<?> modelClass, String property, Object value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.LTE;
        condition.value = value;
        return condition;
    }

    public static Condition createLike(Class<?> modelClass, String property, Class<?> targetModelClass, String targetProperty) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_PROPERTY;
        condition.conditionType = ConditionType.LIKE;
        condition.targetModelClass = targetModelClass;
        condition.targetProperty = targetProperty;
        return condition;
    }

    public static Condition createLike(Class<?> modelClass, String property, Object value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.LIKE;
        condition.value = value;
        return condition;
    }

    public static Condition createEq(Class<?> modelClass, String property, Class<?> targetModelClass, String targetProperty) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_PROPERTY;
        condition.conditionType = ConditionType.EQ;
        condition.targetModelClass = targetModelClass;
        condition.targetProperty = targetProperty;
        return condition;
    }

    public static Condition createEq(Class<?> modelClass, String property, Object value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.EQ;
        condition.value = value;
        return condition;
    }

    public static Condition createNotEq(Class<?> modelClass, String property, Class<?> targetModelClass, String targetProperty) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_PROPERTY;
        condition.conditionType = ConditionType.NOT_EQ;
        condition.targetModelClass = targetModelClass;
        condition.targetProperty = targetProperty;
        return condition;
    }

    public static Condition createNotEq(Class<?> modelClass, String property, Object value) {
        Condition condition = new Condition();
        condition.modelClass = modelClass;
        condition.property = property;
        condition.mode = ConditionMode.TO_VALUE;
        condition.conditionType = ConditionType.NOT_EQ;
        condition.value = value;
        return condition;
    }
}
