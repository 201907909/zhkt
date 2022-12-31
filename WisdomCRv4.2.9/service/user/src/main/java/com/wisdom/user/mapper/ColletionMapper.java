package com.wisdom.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface ColletionMapper {

    ArrayList<Long> getCouIdByStuId(@Param("stuId")Long stuId);

    void deleted(@Param("stuId") Long stuId, @Param("couId") Long couId);

    void add(@Param("stuId") Long stuId, @Param("couId") Long couId);


    int isCollection(@Param("stuId") Long stuId, @Param("couId") Long couId);

    void updateCouCollNum( @Param("couId") Long couId, @Param("couCollNum") Long couCollNum);
}
