package com.ibingbo.boot.mapper;

import com.ibingbo.boot.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by bing on 17/5/27.
 */
@Mapper
public interface UserMapper {

    @Select("select * from user")
    List<User> getUsers();
}
