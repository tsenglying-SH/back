package com.shu.mapper;

import com.shu.pojo.MyFriend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MyFriendMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_my_friend
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_my_friend
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    int insert(MyFriend record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_my_friend
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    MyFriend selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_my_friend
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    List<MyFriend> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_my_friend
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    int updateByPrimaryKey(MyFriend record);
}