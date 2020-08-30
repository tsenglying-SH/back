package com.shu.mapper;

import com.shu.pojo.MyFriend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MyFriendMapper {

    int deleteByPrimaryKey(String id);


    int insert(MyFriend record);


    MyFriend selectByPrimaryKey(String id);


    List<MyFriend> selectAll();


    int updateByPrimaryKey(MyFriend record);

    // 根据两个用户id 查找这条记录是否存在
    MyFriend selectOne(String myUserId,String myFriendUserId);
}