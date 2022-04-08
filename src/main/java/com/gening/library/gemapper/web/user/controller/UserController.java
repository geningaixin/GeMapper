/*
package com.gening.library.gemapper.web.user.controller;

import com.gening.library.gemapper.common.mapper.dynamicsql.entity.Condition;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.ConditionType;
import com.gening.library.gemapper.web.user.enums.Sex;
import com.gening.library.gemapper.web.user.enums.State;
import com.gening.library.gemapper.web.user.model.UserInfo;
import com.gening.library.gemapper.web.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping("/insert_auto")
    public String insertAotu() {
        // 将id改为auto，Mysql数据库主键加上去增长
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("wangyajun111");
        userInfo.setPassWord("123456");
        userInfo.setState(State.NORMAL);
        userInfo.setSex(Sex.FEMALE);
        //this.userService.insertContainAutoKey(userInfo);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUserName("wangyajun222");
        userInfo2.setPassWord("123456");
        userInfo2.setState(State.DELETE);
        userInfo2.setSex(Sex.MALE);
        //this.userService.insertSelectiveContainAutoKey(userInfo2);
        return userInfo2.getId().toString();
    }

    @RequestMapping("/insert")
    public String insert() {
        // 将id改为 Insert Mysql数据库主键去除自增长
        UserInfo userInfo = new UserInfo();
        String wkt = "MULTIPOLYGON (((117.02788028035073 23.42164965041112, 117.0278160300858 23.421565179789244, 117.02776094031528 23.421607089995216, 117.02782517978822 23.42169155971781, 117.02788028035073 23.42164965041112)))";
        userInfo.setId(12);
        userInfo.setUserName("wangyajun111");
        userInfo.setPassWord("123456");
        userInfo.setState(State.NORMAL);
        userInfo.setSex(Sex.FEMALE);
        this.userService.insert(userInfo);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(13);
        userInfo2.setUserName("wangyajun222");
        userInfo2.setPassWord("123456");
        userInfo2.setState(State.DELETE);
        userInfo2.setSex(Sex.MALE);
        this.userService.insertSelective(userInfo2);
        return userInfo2.getId().toString();
    }

    @RequestMapping("/update")
    public String update() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(12);
        userInfo.setUserName("wangyajun12");
        userInfo.setPassWord("123456");
        userInfo.setState(State.DELETE);
        userInfo.setSex(Sex.FEMALE);
        this.userService.updateSelective(userInfo);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(13);
        this.userService.update(userInfo2);

        UserInfo userInfo3 = new UserInfo();
        userInfo3.setUserName("wangyajun789");
        userInfo3.addCondition(Condition.create().setModelAndProperty("userInfo.userName").setConditionType(ConditionType.EQ).setValue("wangyajun"));
        this.userService.updateSelective(userInfo3);

        return "success";
    }

    @RequestMapping("/delete")
    public String delete() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(12);
        this.userService.delete(userInfo);

        UserInfo userInfo3 = new UserInfo();
        userInfo3.setUserName("wangyajun789");
        userInfo3.addCondition(Condition.create().setModelAndProperty("userInfo.userName").setConditionType(ConditionType.EQ).setValue("wangyajun789"));
        this.userService.delete(userInfo3);

        return "success";
    }

    @RequestMapping("/findByName")
    public List<UserInfo> findByName() {
        // xml中定义查询方法
        return this.userService.findByName("WANGWANGWANG");
    }

    @RequestMapping("/findById")
    public UserInfo findById() {
        return this.userService.findById(1);
    }

    @RequestMapping("/findAll")
    public List<UserInfo> findAll() {
        return this.userService.findAll();
    }
}
*/
