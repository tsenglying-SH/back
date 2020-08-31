package com.shu.mapper;

import com.shu.pojo.FrientRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface FrientRequestMapper {

    int deleteByPrimaryKey(String id);

    int insert(FrientRequest record);

    FrientRequest selectByPrimaryKey(String id);

    List<FrientRequest> selectAll();

    int updateByPrimaryKey(FrientRequest record);

    // 根据sendUserId和acceptUserId 查询这条记录是否存在
     FrientRequest selectOne(String sendUserId, String acceptUserId);

    // 根据sendUserId和acceptUserId 删除这条记录
    void deleteBySendAndAccept(String sendUserId, String acceptUserId);
}