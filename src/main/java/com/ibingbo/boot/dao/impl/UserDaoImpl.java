package com.ibingbo.boot.dao.impl;

import com.ibingbo.boot.bean.User;
import com.ibingbo.boot.dao.UserDao;
import com.ibingbo.boot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bing on 17/5/27.
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserMapper mapper;
    @Override
    public List<User> getUsers() {
        return this.mapper.getUsers();
    }
}
