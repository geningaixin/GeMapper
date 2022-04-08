package com.gening.library.gemapper.web.user.controller;

import com.gening.library.gemapper.common.mapper.dynamicsql.entity.Condition;
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
 * @className UpdateController
 * @description TODO
 * @date 2022/4/6 15:09
 */
@RestController()
public class UpdateController {

    @Resource
    private UserService userService;

    /**
     * 通过ID修改部分
     */
    @RequestMapping("/update1")
    public String update1() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setId(45))
                .peek(u -> u.setPassWord("1234567890"))
                .peek(u -> u.setState(State.NORMAL))
                .peek(u -> u.setSex(Sex.MALE))
                .findFirst().get();
        userService.updateSelective(userInfo);
        return "success";
    }

    /**
     * 通过ID修改全部
     */
    @RequestMapping("/update2")
    public String update2() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.setId(45))
                .peek(u -> u.setPassWord("0987654321"))
                .findFirst().get();
        userService.update(userInfo);
        return "success";
    }

    /**
     * 通过条件进行修改
     */
    @RequestMapping("/update3")
    public String update3() {
        UserInfo userInfo = Stream.of(new UserInfo())
                .peek(u -> u.addCondition(Condition.createNotNull(UserInfo.class, "passWord")))
                .peek(u -> u.setPassWord("0987654321"))
                .peek(u -> u.setUserName("ruiyechun"))
                .peek(u -> u.setRealName("芮叶淳"))
                .peek(u -> u.setCreateTime(new Date()))
                .peek(u -> u.setSex(Sex.MALE))
                .peek(u -> u.setState(State.NORMAL))
                .findFirst().get();
        userService.updateSelective(userInfo);
        return "success";
    }
}
