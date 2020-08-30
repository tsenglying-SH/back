package com.shu.service.impl;

import com.shu.enums.SearchFriendsStatusEnum;
import com.shu.mapper.MyFriendMapper;
import com.shu.mapper.UserMapper;
import com.shu.pojo.MyFriend;
import com.shu.pojo.User;
import com.shu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Tsenglying
 * @Date 2020/8/25 13:55
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MyFriendMapper myFriendMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExit(String username) {
        User user = new User();
        user.setUsername(username);
        User result = userMapper.selectOne(user);
        return result != null ? true : false;

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserForLogin(String username, String psw) {
        User user1 = new User();
        user1.setUsername(username);
        user1.setPassword(psw);
        User result1 = userMapper.varifyUser(user1);
        return result1;
    }

    @Override
    public User saveUser(User user) {
        // 使用UUID生成32位不重复的字符，截取其中的后12位作为userID
        String userId = UUID.randomUUID().toString().substring(24);
        user.setPassword(user.getPassword());
        user.setNickName(user.getUsername());
        user.setFaceImg("");
        user.setFaceImgBig("");
        //生成二维码
        user.setQrcode("");
        user.setId(userId);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User updateUserInfo(User user) {
        userMapper.updateByPrimaryKey(user);
        return userMapper.selectOne(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        User user = queryUserInfoByUsername(friendUsername);

        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        if (user == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }

        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        if (user.getId().equals(myUserId)) {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }

        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        MyFriend myFriendRes = myFriendMapper.selectOne(myUserId, user.getId());
        if(myFriendRes!=null){
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }
        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfoByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        User result = userMapper.selectOne(user);
        return result;
    }
}
