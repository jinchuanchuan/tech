package com.youcan.tech.service;

import com.youcan.tech.entity.TUser;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public TUser getUserById(Integer id);
}
