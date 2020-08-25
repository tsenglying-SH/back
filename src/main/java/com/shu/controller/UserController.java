package com.shu.controller;

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


}
