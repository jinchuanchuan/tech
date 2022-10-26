package com.youcan.tech.service.impl;

import com.youcan.tech.dao.TUserMapper;
import com.youcan.tech.entity.TUser;
import com.youcan.tech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TUserMapper userMapper;

    @Override
    public TUser getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
