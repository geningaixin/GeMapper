package com.gening.library.gemapper.web.user.controller;

import com.gening.library.gemapper.common.mapper.dynamicsql.entity.Condition;
import com.gening.library.gemapper.web.user.enums.Sex;
import com.gening.library.gemapper.web.user.enums.State;
import com.gening.library.gemapper.web.user.model.UserInfo;
import com.gening.library.gemapper.web.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className SelectController
 * @description TODO
 * @date 2022/4/6 17:49
 */
@RestController()
public class SelectController {

    @Resource
    private UserService userService;

    /**
     * 按ID查询
     */
    @RequestMapping("/select1")
    public String select1() {
        return userService.findById(45).toString();
    }

    /**
     * 查所有
     */
    @RequestMapping("/select2")
    public String select2() {
        return userService.findAll().toString();
    }

    /**
     * 条件查询-按自身属性查询
     */
    @RequestMapping("/select3")
    public String select3() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setUserName("shizhicheng"))
                .peek(u -> u.setPassWord("password"))
                .peek(u -> u.setState(State.NORMAL))
                .peek(u -> u.setSex(Sex.MALE))
                .findFirst().get();
        return userService.findList(userInfo).toString();
    }

    /**
     * 条件查询-按自身属性查询（分页）
     */
    @RequestMapping("/select4")
    public String select4(Integer pageNum) {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setPassWord("password"))
                .findFirst().get();
        return userService.findList(pageNum, 2, userInfo).toString();
    }

    /**
     * 条件查询-按自身属性查询（分页对象）
     */
    @RequestMapping("/select5")
    public String select5(Integer pageNum) {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setPassWord("password"))
                .findFirst().get();
        return userService.findPageInfo(pageNum, 2, userInfo).toString();
    }

    /**
     * 条件查询-按给定条件查询
     */
    @RequestMapping("/select6")
    public String select6() {
        // EQ
        UserInfo user1 = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createEq(UserInfo.class, "userName", "ruiyechun")))
                .peek(u -> u.addCondition(Condition.createEq(UserInfo.class, "userName", UserInfo.class, "passWord")))
                .findFirst().get();
        userService.findList(user1).forEach(System.out::println);

        // NOT_EQ
        UserInfo user2 = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createNotEq(UserInfo.class, "userName", "ruiyechun")))
                .findFirst().get();
        userService.findList(user2).forEach(System.out::println);

        // NOT_NULL
        UserInfo user3 = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createNotNull(UserInfo.class, "passWord")))
                .findFirst().get();
        userService.findList(user3).forEach(System.out::println);

        // IS_NULL
        UserInfo user4 = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createIsNull(UserInfo.class, "createTime")))
                .findFirst().get();
        userService.findList(user4).forEach(System.out::println);

        // IN
        UserInfo user5 = Stream.of(new UserInfo())
                // 字符串IN
                .peek(u -> u.addCondition(Condition.createIn(UserInfo.class, "userName", new String[]{"wangyajun", "ruiyechun"})))
                // 数字IN
                .peek(u -> u.addCondition(Condition.createIn(UserInfo.class, "sex", new Integer[]{1})))
                .findFirst().get();
        userService.findList(user5).forEach(System.out::println);

        // NOT IN
        UserInfo user6 = Stream.of(new UserInfo())
                // 字符串IN
                .peek(u -> u.addCondition(Condition.createNotIn(UserInfo.class, "userName", new String[]{"史志成", "sunyu"})))
                // 数字IN
                .peek(u -> u.addCondition(Condition.createNotIn(UserInfo.class, "sex", new Integer[]{0, 3})))
                .findFirst().get();
        userService.findList(user6).forEach(System.out::println);

        // GT GTE
        UserInfo user7 = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createGt(UserInfo.class, "state", 3)))
                .peek(u -> u.addCondition(Condition.createGte(UserInfo.class, "sex", 4)))
                .findFirst().get();
        userService.findList(user7).forEach(System.out::println);

        // LT LTE
        UserInfo user8 = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createLt(UserInfo.class, "state", 3)))
                .peek(u -> u.addCondition(Condition.createLte(UserInfo.class, "sex", 4)))
                .findFirst().get();
        userService.findList(user8).forEach(System.out::println);

        // LIKE
        UserInfo user9 = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createLike(UserInfo.class, "userName",  "yajun")))
                .peek(u -> u.addCondition(Condition.createLike(UserInfo.class, "userName",  UserInfo.class, "passWord")))
                .findFirst().get();
        userService.findList(user9).forEach(System.out::println);
        return null;
    }
}
