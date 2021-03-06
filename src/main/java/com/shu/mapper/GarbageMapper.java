package com.shu.mapper;

import com.shu.pojo.Garbage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GarbageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garbage
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    int deleteByPrimaryKey(String garbageName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garbage
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    int insert(Garbage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garbage
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    Garbage selectByPrimaryKey(String garbageName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garbage
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    List<Garbage> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_garbage
     *
     * @mbggenerated Tue Aug 25 12:48:59 CST 2020
     */
    int updateByPrimaryKey(Garbage record);
}