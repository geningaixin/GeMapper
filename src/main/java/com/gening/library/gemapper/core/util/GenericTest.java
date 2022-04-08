package com.gening.library.gemapper.core.util;

import com.gening.library.gemapper.web.user.dao.UserInfoMapper;
import com.gening.library.gemapper.web.user.model.UserInfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author G
 * @version 1.0
 * @className GenericTest
 * @description TODO
 * @date 2022/4/1 13:30
 */
public class GenericTest {
    public Map<String, Object> list = new HashMap<>();

    public static void main(String[] args) throws SecurityException, NoSuchFieldException {
        ParameterizedType pt = (ParameterizedType) GenericTest.class.getField(
                "list").getGenericType();
        System.out.println(pt.getActualTypeArguments().length);
        System.out.println(pt.getActualTypeArguments()[0] + " | " + pt.getActualTypeArguments()[1]);

        Map<String, Object> list2 = new HashMap<>();
        System.out.println(list2.getClass());

        System.out.println(BeanUtils.getGeneric(UserInfoMapper.class));
    }

}
