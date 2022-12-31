package com.wisdom.user.feign;

import com.wisdom.user.bean.dto.AuditImgDTO;
import com.wisdom.user.bean.dto.AuditTextDTO;
import com.wisdom.user.bean.vo.ScoreClassVO;
import com.wisdom.user.bean.vo.StudentDetailedVO;
import com.wisdom.user.bean.vo.StudentProgressVO;
import com.wisdom.user.bean.vo.TeacherDetailedVO;
import com.wisdom.user.feign.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@FeignClient(value = "user", name = "user", fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    @GetMapping("/student/selectStudentName")
    List<String> selectStudentName(@RequestBody List<Long> stuIds);
    @GetMapping("/student/queryStudentByClassId")
    List<StudentDetailedVO> queryStudentByClassId(@RequestParam("classId") Long classId);
    @GetMapping("/student/queryStudentProgressByStuId")
    StudentProgressVO queryStudentProgressByStuId(@RequestParam("stuId") Long stuId);
    @GetMapping("/teacher/getTeaNameByTeaId")
    String getTeaNameByTeaId(@RequestParam("teaId") Long teaId);
    @PostMapping("/student/classQueryStuScore")
    List<ScoreClassVO> classQueryStuScore(@RequestParam("stuId") Long stuId);
    @PostMapping("/student/updateStuScore")
    Boolean updateStuScore(@RequestParam(value = "stuScore") Long stuScore, @RequestParam(value = "scoreType") String scoreType);
    @GetMapping("/teacher/getTeaByTeaId")
    TeacherDetailedVO getTeaByTeaId(@RequestParam("teaId") Long teaId);
    @GetMapping("/student/getStuByTeaId")
    StudentDetailedVO getStuByTeaId(@RequestParam("stuId") Long stuId);
    @GetMapping("/student/getStuByStuId")
    StudentDetailedVO getStuByStuId(@RequestParam("stuId") Long stuId);
    @GetMapping("/colletion/isCollection")
    Integer isCollection(@RequestParam("stuId") Long stuId, @RequestParam("couId") Long couId);
    @GetMapping("/cart/isAddCart")
    Integer isAddCart(@RequestParam("stuId") Long stuId, @RequestParam("couId") Long couId);

    @GetMapping("/cart/isBuy")
    Integer isBuy(@RequestParam("stuId") Long stuId, @RequestParam("couId") Long couId);

    @PostMapping("/admin/addAuditText")
    void addAuditText(@RequestBody AuditTextDTO auditTextDTO);

    @PostMapping("/admin/addAuditImg")
    void addAuditImg(@RequestBody AuditImgDTO auditImgDTO);

    @PostMapping("/notice/addNotice")
    void addNotice(@RequestParam("userId") Long userId, @RequestParam("noticeId") Long noticeId);

    @GetMapping("/student/getStuScoreByStuId")
    int getStuScoreByStuId(@RequestParam("stuId") Long stuId);

    @PostMapping("/student/updateScore")
    void updateScore(@RequestParam("stuId") Long stuId, @RequestParam("stuScore") int stuScore);

}
