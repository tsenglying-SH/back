package com.shu.service;

import com.shu.pojo.User;

public interface UserService {

    // 判断用户名是否存在
    boolean queryUserNameIsExit(String username);

    // 查询用户是否存在
    User queryUserForLogin(String username , String psw);

    // 用户注册
    User saveUser(User user);

    // 更新用户信息
    User updateUserInfo(User user);

    // 搜索朋友的前置条件
    Integer preconditionSearchFriends(String myUserId, String friendUsername);

    // 根据姓名查找用户
    User queryUserInfoByUsername(String username);

}
