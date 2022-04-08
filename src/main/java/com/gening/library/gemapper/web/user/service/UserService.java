package com.gening.library.gemapper.web.user.service;

import com.gening.library.gemapper.common.mapper.GeService;
import com.gening.library.gemapper.web.user.model.UserInfo;

import java.util.List;

public interface UserService extends GeService<UserInfo, Integer> {

    List<UserInfo> findByName(String userName);
}
