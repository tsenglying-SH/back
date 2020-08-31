package com.shu.mapper;

import com.shu.pojo.ChatMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ChatMsgMapper {

    int deleteByPrimaryKey(String id);


    int insert(ChatMsg record);


    ChatMsg selectByPrimaryKey(String id);


    List<ChatMsg> selectAll();


    int updateByPrimaryKey(ChatMsg record);

    List<com.shu.pojo.ChatMsg> selectUnsignedMsg(String receiveUserId );
}