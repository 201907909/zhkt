package com.wisdom.stclass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.base.util.R;
import com.wisdom.stclass.bean.dto.*;
import com.wisdom.stclass.bean.vo.ClassAboutVO;
import com.wisdom.stclass.bean.vo.ClassCourseVO;
import com.wisdom.stclass.bean.vo.ClassDetailedVO;
import com.wisdom.stclass.domain.Class;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
public interface ClassService extends IService<Class> {
    R addClass(ClassAddDTO classAddDTO);
    Boolean deleteClassById(Long id);
    R updateClassById(ClassUpdateDTO classUpdateDTO);
    List<ClassAboutVO> queryClassByTeaId(Long teaId);
    ClassDetailedVO queryClassByClaId(Long id);
    ClassCourseVO queryClassCourseByClaId(ClassCourseDTO classCourseDTO);
    R classAddStuList(ClassAddDelStuSDTO classAddDleStuSDTO);
    R classAddCouList(ClassAddDelCouSDTO classAddDelCouSDTO);
    R classFile(MultipartFile multipartFile);
    Boolean classDeletedStuList(ClassAddDelStuSDTO classAddDleStuSDTO);
    Boolean classDeletedCouList(ClassAddDelCouSDTO classAddDelCouSDTO);
    Map<String,Object> getStuDailyTime(Long stuId);
    List<String> getStuClockDate(Long stuId);
    Integer getStuContinuedClock(Long stuId);
}
