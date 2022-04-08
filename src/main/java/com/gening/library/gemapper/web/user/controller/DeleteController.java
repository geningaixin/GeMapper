package com.gening.library.gemapper.web.user.controller;

import com.gening.library.gemapper.common.mapper.dynamicsql.entity.Condition;
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
 * @className DeleteController
 * @description TODO
 * @date 2022/4/1 16:52
 */
@RestController()
public class DeleteController {

    @Resource
    private UserService userService;

    @RequestMapping("/delete1")
    public String delete1() {
        userService.deleteById(43);
        return "success";
    }

    @RequestMapping("/delete2")
    public String delete2() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setUserName("gening"))
                .peek(u -> u.setState(State.DELETE))
                .findFirst().get();

        userService.delete(userInfo);
        return "success";
    }

    @RequestMapping("/delete3")
    public String delete3() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createEq(UserInfo.class, "userName", "wangyajun")))
                .peek(u -> u.addCondition(Condition.createEq(UserInfo.class, "passWord", "password")))
                .findFirst().get();
        userService.delete(userInfo);
        return "success";
    }

}

