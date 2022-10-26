package com.youcan.tech.controller;

import com.youcan.tech.entity.TUser;
import com.youcan.tech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @Autowired
    private UserService userService;

    @RequestMapping("hello")
    public String sayHello(@RequestParam Integer id) {
        TUser user = userService.getUserById(id);
        return user.getName();
    }
}
