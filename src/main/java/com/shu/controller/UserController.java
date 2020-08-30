package com.shu.controller;

import com.shu.enums.SearchFriendsStatusEnum;
import com.shu.pojo.User;
import com.shu.pojo.vo.UserVO;
import com.shu.service.UserService;
import com.shu.util.JSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Tsenglying
 * @Date 2020/8/24 17:01
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册/登录模块
    @PostMapping("/registOrLogin")
    public JSONResult registOrLogin(@RequestBody User user) {

        // 1,判断用户名和密码不为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }

        // 2.判断用户名是否存在，若存在就登录，否则注册
        boolean userIsExist = userService.queryUserNameIsExit(user.getUsername());
        User userResult = null;
        if (userIsExist) {
            // 2.1 登录
            userResult = userService.queryUserForLogin(user.getUsername(), user.getPassword());

            if (userResult == null) {
                return JSONResult.errorMsg("用户名或密码不正确");
            }
        } else {
            // 2.2 注册
            userResult = userService.saveUser(user);
        }
        // 专门的返回对象返给前端，去掉了User中的password等敏感信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userResult, userVO);
        return JSONResult.ok(userVO);
    }


    // 修改昵称
    @PostMapping("/setNickname")
    public JSONResult setNickname(@RequestBody User user) throws Exception {

        User user2 = new User();
        user2.setId(user.getId());
        user2.setNickName(user.getNickName());

        User result = userService.updateUserInfo(user2);

        return JSONResult.ok(result);
    }


    // 搜索好友
    @PostMapping("/search")
    public JSONResult searchUser( String myUserId, String friendUserName) throws Exception {

        // 0. 判断 myUserId friendUsername 不能为空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUserName)) {
            return JSONResult.errorMsg("");
        }
        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Integer status = userService.preconditionSearchFriends(myUserId, friendUserName);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            User user = userService.queryUserInfoByUsername(friendUserName);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return JSONResult.ok(userVO);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return JSONResult.errorMsg(errorMsg);
        }
    }

}
