package com.gening.library.gemapper.web.user.dao;

import com.gening.library.gemapper.common.mapper.GeMapper;
import com.gening.library.gemapper.web.user.model.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserInfoMapper extends GeMapper<UserInfo, Integer> {

    UserInfo findById();

    List<UserInfo> findByName(@Param("userName") String userName);
}
