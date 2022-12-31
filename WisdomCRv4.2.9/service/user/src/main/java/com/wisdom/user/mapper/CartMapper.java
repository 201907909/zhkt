package com.wisdom.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CartMapper {

    List<Long> getCouIdByStuId(@Param("stuId") Long stuId);

    void deletedCartByCouId(@Param("stuId") Long stuId, @Param("couId") Long couId);

    void deletedCart(@Param("stuId") Long stuId, @Param("coudId") List<Long> couId);

    void buy(@Param("stuId") Long stuId, @Param("couId") Long couId);
    void buy1(@Param("stuId") Long stuId, @Param("couId") Long couId);

    void add(@Param("stuId") Long stuId, @Param("couId") Long couId);

    Integer isAddCart(@Param("stuId") Long stuId, @Param("couId") Long couId);

    Integer isBuy(@Param("stuId") Long stuId, @Param("couId") Long couId);

    void refund(@Param("stuId") Long stuId, @Param("couId") Long couId);
    void addDeScore(@Param("stuId") Long stuId, @Param("scoreType") int scoreType, @Param("score") int score, @Param("deductType") int deductType);
}
