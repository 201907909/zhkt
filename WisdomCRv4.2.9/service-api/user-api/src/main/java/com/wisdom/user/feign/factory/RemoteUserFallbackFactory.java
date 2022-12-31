package com.wisdom.user.feign.factory;

import com.wisdom.user.bean.dto.AuditImgDTO;
import com.wisdom.user.bean.dto.AuditTextDTO;
import com.wisdom.user.bean.vo.ScoreClassVO;
import com.wisdom.user.bean.vo.StudentDetailedVO;
import com.wisdom.user.bean.vo.StudentProgressVO;
import com.wisdom.user.bean.vo.TeacherDetailedVO;
import com.wisdom.user.feign.RemoteUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    @Override
    public RemoteUserService create(Throwable throwable) {
        return new RemoteUserService() {
            @Override
            public List<String> selectStudentName(List<Long> stuIds) {
                return null;
            }
            @Override
            public List<StudentDetailedVO> queryStudentByClassId(Long classId) {
                return null;
            }
            @Override
            public StudentProgressVO queryStudentProgressByStuId(Long stuId) {
                return null;
            }
            @Override
            public String getTeaNameByTeaId(Long teaId) {
                return null;
            }

            @Override
            public List<ScoreClassVO> classQueryStuScore(Long stuId) {
                return null;
            }

            @Override
            public Boolean updateStuScore(Long stuScore, String scoreType) {
                return null;
            }

            @Override
            public TeacherDetailedVO getTeaByTeaId(Long teaId) {
                return null;
            }

            @Override
            public StudentDetailedVO getStuByTeaId(Long stuId) {
                return null;
            }

            @Override
            public StudentDetailedVO getStuByStuId(Long stuId) {
                return null;
            }

            @Override
            public Integer isCollection(Long stuId, Long couId) {
                return null;
            }

            @Override
            public Integer isAddCart(Long stuId, Long couId) {
                return null;
            }

            @Override
            public Integer isBuy(Long stuId, Long couId) {
                return null;
            }

            @Override
            public void addAuditText(AuditTextDTO auditTextDTO) {

            }

            @Override
            public void addAuditImg(AuditImgDTO auditImgDTO) {

            }

            @Override
            public void addNotice(Long userId, Long noticeId) {

            }

            @Override
            public int getStuScoreByStuId(Long stuId) {
                return 0;
            }

            @Override
            public void updateScore(Long stuId, int stuScore) {

            }
        };
    }
}
