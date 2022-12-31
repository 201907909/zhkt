package com.wisdom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wisdom.base.util.R;
import com.wisdom.base.util.Sample;
import com.wisdom.base.util.SampleAip;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.user.bean.dto.TeaUpdateDTO;
import com.wisdom.user.bean.dto.UserLoginDTO;
import com.wisdom.user.bean.vo.LoginVO;
import com.wisdom.user.bean.vo.TeacherDetailedVO;
import com.wisdom.user.domain.Teacher;
import com.wisdom.user.mapper.TeaMapper;
import com.wisdom.user.service.TeacherService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private TeaMapper teaMapper;
    @Value("${APP_ID}")
    public String APP_ID;
    @Value("${API_KEY}")
    public String API_KEY;
    @Value("${SECRET_KEY}")
    public String SECRET_KEY;
    @Resource
    private QiniuService qiniuService;
    @Value("${filesonpath}")
    private String fileSonPath;
    private String filePath;
    public TeacherServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }

    @Override
    public Boolean isExist(String email) {
        return teaMapper.exists(new QueryWrapper<Teacher>().eq("email",email));
    }

    @Override
    public LoginVO login(UserLoginDTO userLoginDTO) {
        return teaMapper.login(userLoginDTO);
    }

    @SneakyThrows
    @Override
    public R teaFile(MultipartFile multipartFile){
        if(Objects.isNull(multipartFile)){
            return R.error("请选择要上传的文件");
        }
        if(multipartFile.getSize() > 1024*1024*1024){
            return R.error("文件大小不能超过10M！");
        }
        //获取文件后缀
        assert multipartFile.getContentType() != null;
        if(multipartFile.getContentType().contains("image/png, image/jpeg, image/gif")){
            return R.error("请选择jpg,png格式的图片");
        }
        File savePathFile = new File(filePath+fileSonPath);
        if(!savePathFile.exists()){
            //若不存在该目录，则创建目录
            savePathFile.mkdirs();
        }
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0,0);
        String filename = idWorker.nextId()+multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File(filename));
        return R.data(filename, "图片上传成功");
    }

    @Override
    public TeacherDetailedVO getTeaDetails(Long id) {
        Teacher teacher = teaMapper.selectOne(new LambdaQueryWrapper<>(Teacher.class).eq(Teacher::getId, id));
        TeacherDetailedVO teacherDetailedVO = new TeacherDetailedVO();
        teacherDetailedVO.setTeaName(teacher.getTeaName())
                .setEmail(teacher.getEmail())
                .setImage(teacher.getImage())
                .setPhone(teacher.getPhone())
                .setGender(teacher.getGender())
                .setCourseNum(teacher.getCourseNum());
        return teacherDetailedVO;
    }

    @Override
    public String getTeaDetailsP(Long teaId) {
        Teacher teacher = teaMapper.selectOne(new LambdaQueryWrapper<>(Teacher.class).eq(Teacher::getTeaId, teaId));
        return teacher.getTeaName();
    }

    @Override
    public R updateInfo(TeaUpdateDTO teaUpdateDTO) {
        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);
        int conclusionType = Sample.sampleImage(sampleAip.getAip(), teaUpdateDTO.getNewImage());
        if (conclusionType != 1) {
            return R.error("图片存在违规");
        }
        Teacher teacher = new Teacher();
        if (teaUpdateDTO.getNewImage() != null){
            String filename = teaUpdateDTO.getOldImage();
//            File file = new File(filePath+fileSonPath+"\\"+filename);
//            // 判断目录或文件是否存在
//            if (!file.exists()) {  // 不存在返回 false
//                return R.error("文件不存在");
//            } else {
//                // 判断是否为文件
//                if (file.isFile()) {  // 为文件时调用删除文件方法
//                    file.delete();
//                }
//            }
            String[] path = filename.split("/");
            String key = path[3];
            try {
                qiniuService.deleteFile(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        teacher.setGender(teaUpdateDTO.getGender())
                .setImage(teaUpdateDTO.getNewImage())
                .setPhone(teaUpdateDTO.getPhone())
                .setTeaName(teaUpdateDTO.getTeaName())
                .setId(teaUpdateDTO.getId());
        if(teaMapper.updateById(teacher)==1) {
            return R.ok("修改用户信息成功");
        }else {
            return R.error("修改用户信息失败");
        }
    }

    @Override
    public Boolean updatePassword(String password, Long id) {
        Teacher teacher = new Teacher();
        teacher.setPassword(password)
                .setId(id);
        if(teaMapper.updateById(teacher)==1) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean forgetPassword(String email, String password) {
        UpdateWrapper<Teacher> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email",email);
        Teacher teacher = new Teacher();
        teacher.setPassword(password);
        if (teaMapper.update(teacher, updateWrapper)==1) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean updateCourseNum(Long id) {
        Teacher teacher = teaMapper.selectById(id);
        teacher.setCourseNum(teacher.getCourseNum()+1)
                .setId(id);
        if(teaMapper.updateById(teacher)==1) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public TeacherDetailedVO getTeaByTeaId(Long teaId) {
        return teaMapper.getTeaByTeaId(teaId);
    }



}
