package com.wisdom.user.service;

import com.wisdom.base.util.R;
import com.wisdom.user.bean.dto.TeaUpdateDTO;
import com.wisdom.user.bean.dto.UserLoginDTO;
import com.wisdom.user.bean.vo.LoginVO;
import com.wisdom.user.bean.vo.TeacherDetailedVO;
import org.springframework.web.multipart.MultipartFile;

public interface TeacherService {
    Boolean isExist(String email);
    LoginVO login(UserLoginDTO userLoginDTO);
    TeacherDetailedVO getTeaDetails(Long id);
    String getTeaDetailsP(Long teaId);
    R updateInfo(TeaUpdateDTO teaUpdateDTO);
    Boolean updatePassword(String password, Long id);
    Boolean forgetPassword(String email,String password);
    Boolean updateCourseNum(Long id);
    R teaFile(MultipartFile multipartFile);
    TeacherDetailedVO getTeaByTeaId(Long teaId);
}
