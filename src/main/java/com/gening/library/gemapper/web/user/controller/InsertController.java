package com.gening.library.gemapper.web.user.controller;

import com.gening.library.gemapper.web.user.enums.Sex;
import com.gening.library.gemapper.web.user.enums.State;
import com.gening.library.gemapper.web.user.model.UserInfo;
import com.gening.library.gemapper.web.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className InsertController
 * @description TODO
 * @date 2022/4/1 15:33
 */
@RestController()
public class InsertController {

    @Resource
    private UserService userService;

    @RequestMapping("/insert1")
    public String insert1() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setUserName("shizhicheng"))
                .peek(u -> u.setPassWord("password"))
                .peek(u -> u.setRealName(null))
                .peek(u -> u.setCreateTime(new Date()))
                .peek(u -> u.setState(State.NORMAL))
                .peek(u -> u.setSex(Sex.MALE))
                .findFirst().get();
        userService.insert(userInfo);
        return String.valueOf(userInfo.getId());
    }

    @RequestMapping("/insert2")
    public String insert2() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setUserName("wangyajun"))
                .peek(u -> u.setPassWord("password"))
                .peek(u -> u.setRealName(null))
                .peek(u -> u.setCreateTime(new Date()))
                .peek(u -> u.setState(State.DELETE))
                .peek(u -> u.setSex(Sex.FEMALE))
                .findFirst().get();
        userService.insertToUseGeneratedKey(userInfo);
        return String.valueOf(userInfo.getId());
    }

    @RequestMapping("/insert3")
    public String insert3() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setUserName("ruiyechun"))
                .peek(u -> u.setPassWord("password"))
                .peek(u -> u.setRealName(null))
                .peek(u -> u.setCreateTime(new Date()))
                .peek(u -> u.setState(State.DELETE))
                .peek(u -> u.setSex(Sex.FEMALE))
                .findFirst().get();
        userService.insertSelective(userInfo);
        return String.valueOf(userInfo.getId());
    }

    @RequestMapping("/insert4")
    public String insert4() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setUserName("gening"))
                .peek(u -> u.setPassWord("password"))
                .peek(u -> u.setRealName(null))
                .peek(u -> u.setCreateTime(new Date()))
                .peek(u -> u.setState(State.DELETE))
                .peek(u -> u.setSex(Sex.FEMALE))
                .findFirst().get();
        userService.insertSelectiveToUseGeneratedKey(userInfo);
        return String.valueOf(userInfo.getId());
    }
}
