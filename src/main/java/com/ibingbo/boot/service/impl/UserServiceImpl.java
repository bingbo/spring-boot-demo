package com.ibingbo.boot.service.impl;

import com.ibingbo.boot.bean.User;
import com.ibingbo.boot.dao.UserDao;
import com.ibingbo.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bing on 17/5/27.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public List<User> getUsers() {
        return this.userDao.getUsers();
    }
}
