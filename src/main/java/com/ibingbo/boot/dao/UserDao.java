package com.ibingbo.boot.dao;

import com.ibingbo.boot.bean.User;

import java.util.List;

/**
 * Created by bing on 17/5/27.
 */
public interface UserDao {
    List<User> getUsers();
}
