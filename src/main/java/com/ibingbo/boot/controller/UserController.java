package com.ibingbo.boot.controller;

import com.ibingbo.boot.bean.User;
import com.ibingbo.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by bing on 17/5/27.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index() {
        return "this is user index page...!";
    }

    @RequestMapping("/list")
    public List<User> list() {
        return this.userService.getUsers();
    }
}
