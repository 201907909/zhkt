package com.wisdom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wisdom.base.util.R;
import com.wisdom.base.util.Sample;
import com.wisdom.base.util.SampleAip;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.course.feign.RemoteCourseService;
import com.wisdom.stclass.feign.RemoteClassService;
import com.wisdom.user.bean.dto.ScoreQueryDTO;
import com.wisdom.user.bean.dto.StuUpdateDTO;
import com.wisdom.user.bean.dto.UserLoginDTO;
import com.wisdom.user.bean.vo.*;
import com.wisdom.user.domain.StuScore;
import com.wisdom.user.domain.Student;
import com.wisdom.user.mapper.StudentMapper;
import com.wisdom.user.mapper.StudentScoreMapper;
import com.wisdom.user.service.StudentService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StudentScoreMapper studentScoreMapper;
    @Resource
    private RemoteCourseService remoteCourseService;
    @Resource
    private RemoteClassService remoteClassService;
    @Resource
    private QiniuService qiniuService;
    @Value("${APP_ID}")
    public String APP_ID;
    @Value("${API_KEY}")
    public String API_KEY;
    @Value("${SECRET_KEY}")
    public String SECRET_KEY;

    @Value("${filesonpath}")
    private String fileSonPath;
    private String filePath;
    public StudentServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// ?????????????????????
    }

    @Override
    public Boolean isExist(String email) {
        return studentMapper.exists(new QueryWrapper<Student>().eq("email",email));
    }

    @SneakyThrows
    @Override
    public String stuFile(MultipartFile multipartFile){
        if(Objects.isNull(multipartFile)){
            return "???????????????????????????";
        }
        if(multipartFile.getSize() > 1024*1024*1024){
            return "????????????????????????10M???";
        }
        //??????????????????
        assert multipartFile.getContentType() != null;
        if(multipartFile.getContentType().contains("image/png, image/jpeg, image/gif")){
            return "?????????jpg,png???????????????";
        }
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0,0);
        String filename = idWorker.nextId()+multipartFile.getOriginalFilename();
        File savePathFile = new File(filePath+fileSonPath + filename);
        if(!savePathFile.exists()){
            //???????????????????????????????????????
            savePathFile.mkdirs();
        }
        multipartFile.transferTo(savePathFile);
        return filename;
    }

    @Override
    public StudentDetailedVO getStuDetails(Long id) {
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<>(Student.class).eq(Student::getId, id));
        String image = student.getImage();
//        InputStream in = null;
//        byte[] data = null;
//        try {
//            in = new FileInputStream(filePath+fileSonPath+"\\"+image);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //??????Base64??????
        BASE64Encoder encoder = new BASE64Encoder();
        StudentDetailedVO studentDetailedVO = new StudentDetailedVO();
        studentDetailedVO.setStuName(student.getStuName())
                .setEmail(student.getEmail())
                .setImageIO(image)
                .setImage(image)
                .setPhone(student.getPhone())
                .setGender(student.getGender())
                .setStuStudyTime(student.getStuStudyTime())
                .setScoreTotal(student.getStuScore())
                .setStuScore(classQueryStuScore(student.getStuId()));
        return studentDetailedVO;
    }

    @Override
    public LoginVO login(UserLoginDTO userLoginDTO) {
        return studentMapper.login(userLoginDTO);
    }

    @Override
    public R updateInfo(StuUpdateDTO stuUpdateDTO) {
        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);
        int conclusionType = Sample.sampleImage(sampleAip.getAip(), stuUpdateDTO.getNewImage());
        if (conclusionType != 1) {
            return R.error("??????????????????");
        }
        Student student = new Student();
        if (stuUpdateDTO.getNewImage() != null){
            String filename = stuUpdateDTO.getOldImage();
            if (filename != null){
//                File file = new File(filePath+fileSonPath+"\\"+filename);
//                // ?????????????????????????????????
//                if (!file.exists()) {  // ??????????????? false
//                    return R.error("???????????????");
//                } else {
//                    // ?????????????????????
//                    if (file.isFile()) {  // ????????????????????????????????????
//                        file.delete();
//                    }
//                }
                String[] path = filename.split("/");
                String key = path[3];
                try {
                    qiniuService.deleteFile(key);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        student.setGender(stuUpdateDTO.getGender())
                .setImage(stuUpdateDTO.getNewImage())
                .setPhone(stuUpdateDTO.getPhone())
                .setStuName(stuUpdateDTO.getStuName())
                .setId(stuUpdateDTO.getId());
        if(studentMapper.updateById(student)==1) {
            return R.ok("????????????????????????");
        }else {
            return R.error("????????????????????????");
        }
    }

    @Override
    public Boolean updatePassword(String password, Long id) {
        Student student = new Student();
        student.setPassword(password)
                .setId(id);
        if(studentMapper.updateById(student)==1) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean forgetPassword(String email, String password) {
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email",email);
        Student student = new Student();
        student.setPassword(password);
        if (studentMapper.update(student, updateWrapper)==1) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<StudentDetailedVO> queryStudentByClassId(Long classId) {
        List<StudentDetailedVO> studentDetailedVOS = studentMapper.queryStudentByClassId(classId);
        if(studentDetailedVOS.size() != 0){
            for (StudentDetailedVO studentDetailedVO:studentDetailedVOS){
                studentDetailedVO.setStuScore(classQueryStuScore(studentDetailedVO.getStuId()));
            }
        }
        return studentDetailedVOS;
    }

    @Override
    public StudentProgressVO queryStudentProgressByStuId(Long stuId) {
        StudentProgressVO studentDetailedVOS = new StudentProgressVO();
        Map<String,Object> resultMap = remoteClassService.getStuDailyTime(stuId);
        if (resultMap != null){
            studentDetailedVOS.setStuDailyTimeStart((String) resultMap.get("stuDailyTimeStart"))
                    .setStuDailyTime((List<List<Integer>>) resultMap.get("resultTotal"));
        }
        studentDetailedVOS.setStuClockDate(remoteClassService.getStuClockDate(stuId))
                .setStuContinuedClock(remoteClassService.getStuContinuedClock(stuId));
        return studentDetailedVOS;
    }

    @Override
    public Boolean updateStuStudyTime(Long stuStudyTime, Long id) {
        Long oldStuStudyTime = studentMapper.getStuStudyTime(id);
        Student student = new Student();
        student.setStuStudyTime(oldStuStudyTime + stuStudyTime)
                .setId(id);
        if(studentMapper.updateById(student)==1) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<ScoreClassVO> classQueryStuScore(Long stuId) {
        List<Integer> scoreType = studentMapper.getStuScoreType(stuId);
        if (scoreType != null){
            scoreType = scoreType.stream().distinct().collect(Collectors.toList());
            List<ScoreClassVO> scoreClassVOS = new ArrayList<>();
            for (Integer type:scoreType){
                ScoreQueryDTO scoreQueryDTO = new ScoreQueryDTO();
                scoreQueryDTO.setStuId(stuId)
                        .setScoreType(type);
                List<StuScore> stuScoreList = studentMapper.getStuScore(scoreQueryDTO);
                Integer scoreTotal = 0;
                for (StuScore stuScore:stuScoreList){
                    scoreTotal += stuScore.getScore();
                }
                ScoreClassVO scoreClassVO = new ScoreClassVO();
                if (type == 0){
                    scoreClassVO.setValue(scoreTotal)
                            .setName("????????????");
                }else if (type == 1){
                    scoreClassVO.setValue(scoreTotal)
                            .setName("????????????");
                }else if (type == 2){
                    scoreClassVO.setValue(scoreTotal)
                            .setName("????????????");
                }else if (type == 3){
                    scoreClassVO.setValue(scoreTotal)
                            .setName("????????????");
                }else if (type == 4){
                    scoreClassVO.setValue(scoreTotal)
                            .setName("????????????");
                }else if (type == 5){
                    scoreClassVO.setValue(scoreTotal)
                            .setName("????????????");
                }else if (type == 6){
                    scoreClassVO.setValue(scoreTotal)
                            .setName("????????????");
                }
                scoreClassVOS.add(scoreClassVO);
            }
            return scoreClassVOS;
        }else {
            if (studentMapper.addScore(stuId) != null){
                List<ScoreClassVO> scoreClassVOS = new ArrayList<>();
                String[] scoreTypeName = {"????????????","????????????","????????????","????????????","????????????","????????????","????????????"};
                for (int i=0;i<7;i++){
                    ScoreClassVO scoreClassVO = new ScoreClassVO();
                    if (i != 5){
                        scoreClassVO.setValue(0)
                                .setName(scoreTypeName[i]);
                    }else {
                        scoreClassVO.setValue(50)
                                .setName(scoreTypeName[i]);
                    }
                    scoreClassVOS.add(scoreClassVO);
                }
                return scoreClassVOS;
            }
        }
        return null;
    }


    @Override
    public Boolean addClock(Long stuId) {
        if(studentMapper.addClock(stuId) != null) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<StuAboutClassVO> classQueryAllStu(String email) {
        List<StuAboutClassVO> stuAboutClassVOList = studentMapper.classQueryAllStu(email);
        for (StuAboutClassVO stuAboutClassVO:stuAboutClassVOList){
            String image = stuAboutClassVO.getImage();
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath+fileSonPath+"\\"+image);
//                data = new byte[in.available()];
//                in.read(data);
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //??????Base64??????
//            BASE64Encoder encoder = new BASE64Encoder();
//            stuAboutClassVO.setImage("data:image/jpeg;base64,"+encoder.encode(data));
            stuAboutClassVO.setImage(image);
        }
        return stuAboutClassVOList;
    }

    /**
     * ??????????????????
     *
     * @param stuId ??????id
     * @param score ??????
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addScore(Long stuId, Integer score) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("stu_id", stuId);
            queryWrapper.select("stu_score");
            Student student = studentMapper.selectOne(queryWrapper);
            Integer newScore = student.getStuScore() + score;

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("stu_id", stuId);
            updateWrapper.set("stu_score", newScore);
            int result = studentMapper.update(null, updateWrapper);// ???????????????
            if (result == 1) {
                StuScore studentScore = new StuScore();
                studentScore.setStuId(stuId)
                        .setScoreType(0)
                        .setScore(score);
                result = studentScoreMapper.insert(studentScore);// ?????????????????????
                if (result == 1) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * ??????????????????
     *
     * @param stuId ??????id
     * @param score ??????
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean subScore(Long stuId, Integer score) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("stu_id", stuId);
            queryWrapper.select("stu_score");
            Student student = studentMapper.selectOne(queryWrapper);
            Integer newScore = student.getStuScore() - score;
            if (newScore < 0) {// ????????????
                return false;
            }

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("stu_id", stuId);
            updateWrapper.set("stu_score", newScore);
            int result = studentMapper.update(null, updateWrapper);// ???????????????
            if (result == 1) {
                StuScore studentScore = new StuScore();
                studentScore.setStuId(stuId)
                        .setScoreType(6)
                        .setDeductType(0)
                        .setScore(score);
                result = studentScoreMapper.insert(studentScore);// ?????????????????????
                if (result == 1) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public StudentDetailedVO getStuByStuId(Long stuId) {
        return studentMapper.getStuByStuId(stuId);
    }

    @Override
    public List<String> selectStudentName(List<Long> stuIds) {
        List<String> stuNames = new ArrayList<>();
        for (Long stuId:stuIds){
            String stuName = studentMapper.selectStudentName(stuId);
            stuNames.add(stuName);
        }
        return stuNames;
    }
    @Override
    public int getStuScoreByStuId(Long stuId) {
        return studentMapper.getStuScoreByStuId(stuId);
    }

    @Override
    public void updateScore(Long stuId, int stuScore) {
        studentMapper.updateScore(stuId, stuScore);
    }
}
