package com.gening.library.gemapper.web.user.service.impl;

import com.gening.library.gemapper.common.mapper.GeMapper;
import com.gening.library.gemapper.common.mapper.GeServiceImpl;
import com.gening.library.gemapper.web.user.dao.UserInfoMapper;
import com.gening.library.gemapper.web.user.model.UserInfo;
import com.gening.library.gemapper.web.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl extends GeServiceImpl<UserInfo, Integer> implements UserService {

    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    protected GeMapper<UserInfo, Integer> getMapper() {
        return userInfoMapper;
    }

    public List<UserInfo> findByName(String userName) {
        return this.userInfoMapper.findByName(userName);
    }
}
