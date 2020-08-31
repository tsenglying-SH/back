package com.shu.service;

import com.shu.netty.ChatMsg;
import com.shu.pojo.User;
import com.shu.pojo.vo.FriendRequestVO;
import com.shu.pojo.vo.MyFriendVO;

import java.util.List;

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

    // 添加好友请求记录，并保存到数据库
    void sendFriendRequest(String myUserId, String friendUserName);

    // 查询好友请求列表
    List<FriendRequestVO> queryFriendRequest(String acceptUserId );

    //删除好友请求
    void deleteFriendRequest(String sendUserId, String acceptUserId);

    /**
     *             : 通过好友请求
     * 				1. 保存好友
     * 				2. 逆向保存好友
     * 				3. 删除好友请求记录
     */
     void passFriendRequest(String sendUserId, String acceptUserId);


    //查询好友列表
     List<MyFriendVO> queryMyFriends(String userId);

    /**
     * @Description: 保存聊天消息到数据库
     */
    public String saveMsg(ChatMsg chatMsg);

    /**
     * @Description: 批量签收消息
     */
     void updateMsgSigned(List<String> msgIdList);

    /**
     * @Description: 获取未签收消息列表
     */
    public List<com.shu.pojo.ChatMsg> getUnReadMsgList(String acceptUserId);
}
