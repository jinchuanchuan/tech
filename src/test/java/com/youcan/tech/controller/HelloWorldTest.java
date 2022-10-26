package com.youcan.tech.controller; 

import com.youcan.tech.TechApplication;
import com.youcan.tech.entity.TUser;
import com.youcan.tech.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** 
* HelloWorld Tester. 
* 
* @author <Authors name> 
* @since <pre>10�� 26, 2022</pre> 
* @version 1.0 
*/ 
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TechApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldTest {

    @Autowired
    private UserService userService;

    @Before
    public void before() throws Exception { 
    } 
    
    @After
    public void after() throws Exception { 
    } 
    
        /** 
    * 
    * Method: sayHello(@RequestParam Integer id) 
    * 
    */ 
    @Test
    public void testSayHello() throws Exception {
        TUser user = userService.getUserById(2);
        System.out.println("user information: " + user);
    }
}
