package com.shu.service.impl;

import com.shu.mapper.UserMapper;
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
        user.setFaceImg(user.getFaceImg());
        user.setFaceImgBig(user.getFaceImgBig());
        //生成二维码
        user.setQrcode(user.getQrcode());
        user.setId(userId);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User updateUserInfo(User user) {
        userMapper.updateByPrimaryKey(user);
        return userMapper.selectOne(user);
    }

    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        return null;
    }

    @Override
    public User queryUserInfoByUsername(String username) {
        return null;
    }
}
