package com.wisdom.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.base.util.R;
import com.wisdom.course.mapper.PracticeMapper;
import com.wisdom.course.service.StudentPracticeService;
import com.wisdom.practice.bean.dto.StuPracticeDTO;
import com.wisdom.practice.bean.vo.SimplePracticeVO;
import com.wisdom.practice.bean.vo.StuPracticeVO;
import com.wisdom.practice.domain.Practice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class StudentPracticeServiceImpl extends ServiceImpl<PracticeMapper, Practice> implements StudentPracticeService {
    @Resource
    private PracticeMapper practiceMapper;
    @Value("${filesonpath}")
    private String fileSonPath;
    private String filePath;
    public StudentPracticeServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }

    @Override
    public R studentIsJoinPractice(StuPracticeDTO stuPracticeDTO) {
        StuPracticeVO stuPracticeVO = practiceMapper.getStuPractice(stuPracticeDTO);
        if (stuPracticeVO == null){
            // 参加
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            stuPracticeDTO.setCreateTime(formatter.format(date));
            Integer praDateVersion = practiceMapper.getPracticeDateVersion(stuPracticeDTO);
            stuPracticeDTO.setPraDateVersion(praDateVersion)
                    .setActivity(10L);
            if (practiceMapper.joinPractice(stuPracticeDTO) == 1 && practiceMapper.upPracticeDateActivity(stuPracticeDTO) == 1){
                return R.ok("参加成功");
            }else {
                return R.error("参加失败");
            }
        }else {
            if (stuPracticeVO.getDeleted() == 1 && stuPracticeVO.getIsEnd() == 0){
                // 参加
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                stuPracticeDTO.setCreateTime(formatter.format(date));
                Integer praDateVersion = practiceMapper.getPracticeDateVersion(stuPracticeDTO);
                stuPracticeDTO.setPraDateVersion(praDateVersion)
                        .setActivity(10L)
                        .setState(0);
                if (practiceMapper.upJoinPractice(stuPracticeDTO) == 1 && practiceMapper.upPracticeDateActivity(stuPracticeDTO) == 1){
                    return R.ok("参加成功");
                }else {
                    return R.error("参加失败");
                }
            }else if (stuPracticeVO.getDeleted() == 0 && stuPracticeVO.getIsEnd() == 0){
                // 取消参加
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                stuPracticeDTO.setCreateTime(formatter.format(date));
                Integer praDateVersion = practiceMapper.getPracticeDateVersion(stuPracticeDTO);
                stuPracticeDTO.setPraDateVersion(praDateVersion)
                        .setActivity(5L)
                        .setState(1);
                if (practiceMapper.upJoinPractice(stuPracticeDTO) == 1 && practiceMapper.upPracticeDateActivity(stuPracticeDTO) == 1){
                    return R.ok("取消成功");
                }else {
                    return R.error("取消失败");
                }
            }else {
                return R.error(403, "不可重复参加");
            }
        }
    }

    @Override
    public R studentSubmitPractice(StuPracticeDTO stuPracticeDTO) {
        StuPracticeVO stuPracticeVO = practiceMapper.getStuPractice(stuPracticeDTO);
        if (stuPracticeVO.getIsEnd() == 0 && stuPracticeVO.getDeleted() == 0){
            // 提交
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            stuPracticeDTO.setCreateTime(formatter.format(date));
            Integer praDateVersion = practiceMapper.getPracticeDateVersion(stuPracticeDTO);
            Integer praVersion = practiceMapper.getPracticeVersion(stuPracticeDTO.getPraId());
            stuPracticeDTO.setPraDateVersion(praDateVersion)
                    .setActivity(15L + stuPracticeDTO.getScore() * 10L)
                    .setState(1)
                    .setPraVersion(praVersion);
            if (practiceMapper.upSubmitPractice(stuPracticeDTO) == 1
                    && practiceMapper.upPracticeDateActivity(stuPracticeDTO) == 1
                    && practiceMapper.upPracticeGoodNum(stuPracticeDTO) == 1){
                return R.ok("提交成功");
            }else {
                return R.error("提交失败(可能已超过提交时间)");
            }
        }
        return R.error(403, "已完成提交");
    }

    @Override
    public R getAllPracticeSimpleInfo(Long couId) {
        List<SimplePracticeVO> simplePracticeVOList = practiceMapper.getAllPracticeSimpleInfo(couId);
        if (simplePracticeVOList.size() != 0){
            for (SimplePracticeVO simplePracticeVO:simplePracticeVOList){
//                if (!simplePracticeVO.getPraImgName().equals("default")){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath + fileSonPath+"\\"+simplePracticeVO.getPraImgName());
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    simplePracticeVO.setPraImgIO("data:image/jpeg;base64,"+encoder.encode(data));
//                }
                simplePracticeVO.setPraImgIO(simplePracticeVO.getPraImgName());
            }
        }else {
            return R.error("暂不存在实践");
        }
        return R.data(simplePracticeVOList);
    }

    @Override
    public R getPracticeSimpleInfo(Long id, Long stuId) {
        SimplePracticeVO simplePracticeVO = practiceMapper.getPracticeSimpleInfo(id);
        if (simplePracticeVO != null){
            if (simplePracticeVO.getIsEndP() == 0){
                Integer state = practiceMapper.getStuPracticeState(id, stuId);
                Integer isEndS = null;
                if (state == null){
                    state = 1;
                }else {
                    if (state == 0){
                        isEndS = practiceMapper.getStuPracticeIsEnd(id, stuId);
                    }
                }
                Date createTime = simplePracticeVO.getCreateTime();
                Long times = createTime.getTime() + 1000*60*60*24*14 - System.currentTimeMillis();;
                Long day = times/(1000*60*60*24);
                Long hour = (times-day*1000*60*60*24)/(1000*60*60);
                Long minute = (times-day*1000*60*60*24-hour*1000*60*60)/(1000*60);
                simplePracticeVO.setState(state)
                        .setIsEndS(isEndS)
                        .setSurplusTime(day+"天"+hour+"小时"+minute+"分钟");
            }
//            if (!simplePracticeVO.getPraImgName().equals("default")){
//                InputStream in = null;
//                byte[] data = null;
//                try {
//                    in = new FileInputStream(filePath + fileSonPath+"\\"+simplePracticeVO.getPraImgName());
//                    data = new byte[in.available()];
//                    in.read(data);
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //进行Base64编码
//                BASE64Encoder encoder = new BASE64Encoder();
//                simplePracticeVO.setPraImgIO("data:image/jpeg;base64,"+encoder.encode(data));
//            }
            simplePracticeVO.setPraImgIO(simplePracticeVO.getPraImgName());
        }else {
            return R.error("请先参加该实践");
        }
        return R.data(simplePracticeVO);
    }

    @Override
    public R getPracticeSimpleByInfo(String info, Long couId) {
        List<SimplePracticeVO> simplePracticeVOList = practiceMapper.getPracticeSimpleByInfo(info, couId);
        if (simplePracticeVOList.size() != 0){
            for (SimplePracticeVO simplePracticeVO:simplePracticeVOList){
//                if (!simplePracticeVO.getPraImgName().equals("default")){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath + fileSonPath+"\\"+simplePracticeVO.getPraImgName());
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    simplePracticeVO.setPraImgIO("data:image/jpeg;base64,"+encoder.encode(data));
//                }
                simplePracticeVO.setPraImgIO(simplePracticeVO.getPraImgName());
            }
        }else {
            return R.error("未查询到该实践");
        }
        return R.data(simplePracticeVOList);
    }
}
