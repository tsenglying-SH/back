package com.shu.service.impl;

import com.shu.enums.MsgActionEnum;
import com.shu.enums.MsgSignFlagEnum;
import com.shu.enums.SearchFriendsStatusEnum;
import com.shu.mapper.ChatMsgMapper;
import com.shu.mapper.FrientRequestMapper;
import com.shu.mapper.MyFriendMapper;
import com.shu.mapper.UserMapper;
import com.shu.netty.ChatMsg;
import com.shu.netty.DataContent;
import com.shu.netty.UserChannelRel;
import com.shu.pojo.FrientRequest;
import com.shu.pojo.MyFriend;
import com.shu.pojo.User;
import com.shu.pojo.vo.FriendRequestVO;
import com.shu.pojo.vo.MyFriendVO;
import com.shu.service.UserService;
import com.shu.util.JsonUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private FrientRequestMapper frientRequestMapper;
    @Autowired
    private ChatMsgMapper chatMsgMapper;

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

    // 根据姓名查找用户
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfoByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        User result = userMapper.selectOne(user);
        return result;
    }

    // 添加好友请求记录，并保存到数据库
    @Override
    public void sendFriendRequest(String myUserId, String friendUserName) {
        User friend = queryUserInfoByUsername(friendUserName);
        FrientRequest frientRequest = frientRequestMapper.selectOne(myUserId, friend.getId());
        if(frientRequest==null){
            // 使用UUID生成32位不重复的字符，截取其中的后12位作为userID
            String requestId = UUID.randomUUID().toString().substring(24);
            FrientRequest request= new FrientRequest();
            request.setId(requestId);
            request.setSendUserId(myUserId);
            request.setAcceptUserId(friend.getId());
            request.setRequestDateTime(new Date());
            frientRequestMapper.insert(request);
        }
    }


    // 查询好友请求列表
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FriendRequestVO> queryFriendRequest(String acceptUserId) {
        return  userMapper.queryFriendRequest(acceptUserId);
    }


    //删除好友请求
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void deleteFriendRequest(String sendUserId, String acceptUserId) {
        frientRequestMapper.deleteBySendAndAccept(sendUserId,acceptUserId);
    }


    // 通过好友请求
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void passFriendRequest(String sendUserId, String acceptUserId) {

       			//	1. 保存好友
        		//	2. 逆向保存好友
                //  3. 删除好友请求记录
        saveFriends(sendUserId,acceptUserId);
        saveFriends(acceptUserId,sendUserId);
        deleteFriendRequest(sendUserId, acceptUserId);

        Channel sendChannel = UserChannelRel.get(sendUserId);
        if (sendChannel != null) {
            // 使用websocket主动推送消息到请求发起者，更新他的通讯录列表为最新
            DataContent dataContent = new DataContent();
            dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);

            sendChannel.writeAndFlush(
                    new TextWebSocketFrame(
                            JsonUtils.objectToJson(dataContent)));
        }

    }

    // 在t_my_friend保存好友
    @Transactional(propagation = Propagation.REQUIRED)
    protected  void saveFriends(String sendUserId, String acceptUserId) {
        MyFriend myFriend = new MyFriend();
        // 使用UUID生成32位不重复的字符，截取其中的后12位作为userID
        String recordId = UUID.randomUUID().toString().substring(24);
        myFriend.setId(recordId);
        myFriend.setMyFriendUserId(acceptUserId);
        myFriend.setMyUserId(sendUserId);
        myFriendMapper.insert(myFriend);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<MyFriendVO> queryMyFriends(String userId) {
        List<MyFriendVO> myFirends = userMapper.queryMyFriends(userId);
        return myFirends;
    }


    //保存聊天消息到数据库
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMsg chatMsg) {

        com.shu.pojo.ChatMsg msgDB = new com.shu.pojo.ChatMsg();
        String msgId = UUID.randomUUID().toString().substring(24);
        msgDB.setId(msgId);
        msgDB.setReceiveUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreatTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMsg.getMsg());
        chatMsgMapper.insert(msgDB);

        return msgId;
    }

    // 批量签收消息
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> msgIdList) {

        userMapper.batchUpdateMsgSigned(msgIdList);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<com.shu.pojo.ChatMsg> getUnReadMsgList(String receiveUserId) {
/*
        Example chatExample = new Example(com.imooc.pojo.ChatMsg.class);
        Criteria chatCriteria = chatExample.createCriteria();
        chatCriteria.andEqualTo("signFlag", 0);
        chatCriteria.andEqualTo("acceptUserId", acceptUserId);*/

        List<com.shu.pojo.ChatMsg> result = chatMsgMapper.selectUnsignedMsg(receiveUserId);

        return result;
    }
}
