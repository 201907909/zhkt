package com.wisdom.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wisdom.base.util.R;
import com.wisdom.course.bean.vo.CourseAddVO;
import com.wisdom.practice.bean.dto.PracticeAddUpDTO;
import com.wisdom.practice.bean.vo.PracticeEchartsVO;
import com.wisdom.practice.bean.vo.PracticeUpVO;
import com.wisdom.practice.domain.Practice;
import com.wisdom.type.bean.vo.TypeVO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022/11/1
 */
public interface TeacherPracticeService extends IService<Practice> {
    R uploadPicture(MultipartFile multipartFile);
    R deletePicture(String oldImg);
    Boolean addPractice(PracticeAddUpDTO practiceAddUpDTO);
    Boolean deletePractice(Long id, Long teaId);
    R editPractice(PracticeAddUpDTO practiceAddUpDTO);
    List<PracticeUpVO> getAllPracticeInfoByTea();
    PracticeUpVO getPracticeInfoByTea(Long id);
    List<TypeVO> getAllPracticeType();
    List<CourseAddVO> getAllCourseByTeaId(Long teaId);
    List<PracticeUpVO> getPracticeByInfo(String info);
    List<PracticeEchartsVO> getOverduePractice();
}
