package com.wisdom.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.user.bean.dto.UserLoginDTO;
import com.wisdom.user.bean.vo.LoginVO;
import com.wisdom.user.bean.vo.TeacherDetailedVO;
import com.wisdom.user.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TeaMapper extends BaseMapper<Teacher> {
    LoginVO login(UserLoginDTO teaLoginDTO);

    TeacherDetailedVO getTeaByTeaId(@Param("teaId") Long teaId);
}
