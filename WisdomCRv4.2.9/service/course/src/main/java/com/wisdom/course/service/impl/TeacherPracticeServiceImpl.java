package com.wisdom.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.base.util.R;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.course.bean.vo.CourseAddVO;
import com.wisdom.course.feign.RemoteCourseService;
import com.wisdom.course.mapper.PracticeMapper;
import com.wisdom.course.service.TeacherPracticeService;
import com.wisdom.practice.bean.dto.PracticeAddUpDTO;
import com.wisdom.practice.bean.vo.PraClassVO;
import com.wisdom.practice.bean.vo.PracticeEchartsVO;
import com.wisdom.practice.bean.vo.PracticeUpVO;
import com.wisdom.practice.domain.Practice;
import com.wisdom.type.bean.vo.TypeVO;
import com.wisdom.user.feign.RemoteUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WisdomCR
 * @since 2022/11/1
 */
@Service
@Transactional
public class TeacherPracticeServiceImpl extends ServiceImpl<PracticeMapper, Practice> implements TeacherPracticeService {
    @Resource
    private PracticeMapper practiceMapper;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private RemoteCourseService remoteCourseService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private QiniuService qiniuService;
    private Boolean isEnd = false;
    private Boolean isOne = true;
    @Value("${filesonpath}")
    private String fileSonPath;
    private String filePath;
    public TeacherPracticeServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }

    @SneakyThrows
    @Override
    public R uploadPicture(MultipartFile multipartFile){
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
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0,0);
        String filename = idWorker.nextId()+multipartFile.getOriginalFilename();
        File savePathFile = new File(filePath + fileSonPath + filename);
        if(!savePathFile.exists()){
            //若不存在该目录，则创建目录
            savePathFile.mkdirs();
        }
        multipartFile.transferTo(savePathFile);
        return R.data(filename, "图片上传成功");
    }

    @SneakyThrows
    @Override
    public R deletePicture(String oldImg) {
        File file = new File(filePath + fileSonPath+"\\"+oldImg);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return R.error("文件不存在");
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                file.delete();
            }
        }
        return R.ok("图片删除成功");
    }

    @Override
    public Boolean addPractice(PracticeAddUpDTO practiceAddDTO) {
        Practice practice = new Practice();
        practice.setPraId(practiceAddDTO.getPraId())
                .setTeaId(practiceAddDTO.getTeaId())
                .setCouId(practiceAddDTO.getCouId())
                .setPraTypeId(practiceAddDTO.getPraTypeId())
                .setPraName(practiceAddDTO.getPraName())
                .setPraSite(practiceAddDTO.getPraSite())
                .setPraMain(practiceAddDTO.getPraMain())
                .setCreateTime(practiceAddDTO.getCreateTime())
                .setPraImg(practiceAddDTO.getPraImg());
        int result = practiceMapper.insert(practice);
        int resultDate = practiceMapper.addPracticeDate(practiceAddDTO.getPraId());
        stringRedisTemplate.opsForValue().set(String.valueOf(practiceAddDTO.getPraId()), "1",60*60*24*14, TimeUnit.SECONDS);
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    // sleep()：同步延迟数据，并且会阻塞线程
                    if (isOne){
                        Long currentTimestamps=System.currentTimeMillis();
                        Long oneDayTimestamps= Long.valueOf(60*60*24*1000);
                        Long currentDateTimestamps = currentTimestamps-(currentTimestamps+60*60*8*1000)%oneDayTimestamps;
                        Long pastTimestamps = currentTimestamps - currentDateTimestamps;
                        Thread.sleep(60*60*25*1000-(pastTimestamps));     // 当天剩余时间戳加1小时
                        isOne = false;
                    }else {
                        Thread.sleep(1000*60*60*24);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer day = Integer.valueOf(stringRedisTemplate.opsForValue().get(String.valueOf(practiceAddDTO.getPraId())));
                if (day != 14){
                    day++;
                    Integer surplusDay = 15 - day;
                    stringRedisTemplate.opsForValue().set(String.valueOf(practiceAddDTO.getPraId()), String.valueOf(day),60*60*24*surplusDay, TimeUnit.SECONDS);
                    practiceMapper.addPracticeDate(practiceAddDTO.getPraId());
                    //创建定时器
                    Thread newThread = new Thread(this);
                    //开始执行
                    newThread.start();
                }else {
                    isEnd = true;
                }
            }
        };
        //创建定时器
        Thread thread = new Thread(runnable);
        //开始执行
        thread.start();
        if (isEnd){
            thread.interrupt();
        }
        if (result == 1 && resultDate == 1) {
            return true;
        }
        return false;
    }

    @SneakyThrows
    @Override
    public Boolean deletePractice(Long id, Long teaId) {
        QueryWrapper<Practice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("tea_id", teaId);
        if (practiceMapper.getPracticeIsEnd(id) == 1){
            return false;
        }
        Long praId = practiceMapper.getPracticeId(id);
        String praImg = practiceMapper.getPraImgById(id);
        if (remove(queryWrapper) && practiceMapper.deletedPracticeDate(praId) != 0){
            if (!praImg.equals("default")){
//                File file = new File(filePath + fileSonPath+"\\"+praImg);
//                // 判断目录或文件是否存在
//                if (!file.exists()) {  // 不存在返回 false
//                    return false;
//                } else {
//                    // 判断是否为文件
//                    if (file.isFile()) {  // 为文件时调用删除文件方法
//                        file.delete();
//                    }
//                }
                String[] path = praImg.split("/");
                String key = path[3];
                try {
                    qiniuService.deleteFile(key);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public R editPractice(PracticeAddUpDTO practiceAddUpDTO) {
        if (practiceMapper.getPracticeIsEnd(practiceAddUpDTO.getId()) == 1){
            return null;
        }
        Practice practice = new Practice();
        if (practiceAddUpDTO.getPraImg().equals("")){
            practice.setPraImg(practiceAddUpDTO.getOldImg());
        }else {
            practice.setPraImg(practiceAddUpDTO.getPraImg());
        }
        practice.setId(practiceAddUpDTO.getId())
                .setCreateTime(practiceAddUpDTO.getCreateTime())
                .setCouId(practiceAddUpDTO.getCouId())
                .setPraTypeId(practiceAddUpDTO.getPraTypeId())
                .setPraMain(practiceAddUpDTO.getPraMain())
                .setPraName(practiceAddUpDTO.getPraName())
                .setPraSite(practiceAddUpDTO.getPraSite())
                .setTeaId(practiceAddUpDTO.getTeaId());
        if(practiceMapper.updateById(practice)==1) {
            if (!practiceAddUpDTO.getPraImg().equals("") && !practiceAddUpDTO.getOldImg().equals("default")){
//                String filename = practiceAddUpDTO.getOldImg();
//                File file = new File(filePath + fileSonPath+"\\"+filename);
//                // 判断目录或文件是否存在
//                if (!file.exists()) {  // 不存在返回 false
//                    return R.error("文件不存在");
//                } else {
//                    // 判断是否为文件
//                    if (file.isFile()) {  // 为文件时调用删除文件方法
//                        file.delete();
//                    }
//                }
                String[] path = practiceAddUpDTO.getPraImg().split("/");
                String key = path[3];
                try {
                    qiniuService.deleteFile(key);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            PracticeUpVO practiceUpVO = getPracticeInfoByTea(practiceAddUpDTO.getId());
            return R.data(practiceUpVO, "修改实践详情成功");
        }else {
            return R.error("修改实践详情失败");
        }
    }

    // 教师查询所有线下实践任务
    @Override
    public List<PracticeUpVO> getAllPracticeInfoByTea() {
        List<PracticeUpVO> allPracticeVO = practiceMapper.getAllPracticeInfoByTea();
        if (allPracticeVO.size() != 0){
            for(PracticeUpVO practiceUpVO:allPracticeVO){
                if (!practiceUpVO.getPraImgName().equals("default")){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath + fileSonPath+"\\"+practiceUpVO.getPraImgName());
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    practiceUpVO.setPraImgIO("data:image/jpeg;base64,"+encoder.encode(data));
                    practiceUpVO.setPraImgIO(practiceUpVO.getPraImgName());
                }
                practiceUpVO.setEndTime(new Date(practiceUpVO.getCreateTime().getTime()+(1000*60*60*24*14)));
            }
        }else {
            return null;
        }
        return allPracticeVO;
    }

    /**
     * 教师查询指定线下实践任务
     *
     * @param id 雪花id
     * @return {@link Practice}
     */
    @Override
    public PracticeUpVO getPracticeInfoByTea(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id", id);
        wrapper.select("id", "pra_id", "tea_id", "cou_id", "pra_type_id", "pra_name", "pra_site", "pra_main", "pra_img", "good_number", "create_time", "is_end");
        Practice practice = practiceMapper.selectOne(wrapper);
        if (practice.getIsEnd() == 1){
            return null;
        }
        String teaName = remoteUserService.getTeaNameByTeaId(practice.getTeaId());
        List<Long> couIds = new ArrayList<>();
        couIds.add(practice.getCouId());
        List<String> couName = remoteCourseService.selectCourseName(couIds);
        String praType = practiceMapper.getPraCouTypeByPraTypeId(practice.getPraTypeId());
        Integer joinNumber = practiceMapper.getPraJoinNumberByPraId(practice.getPraId());
        Integer completeNumber = practiceMapper.getPraCompleteNumberByPraId(practice.getPraId());
        PracticeUpVO practiceUpVO = new PracticeUpVO();
        if (!practice.getPraImg().equals("default")){
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath + fileSonPath+"\\"+practice.getPraImg());
//                data = new byte[in.available()];
//                in.read(data);
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //进行Base64编码
//            BASE64Encoder encoder = new BASE64Encoder();
//            practiceUpVO.setPraImgIO("data:image/jpeg;base64,"+encoder.encode(data));
            practiceUpVO.setPraImgIO(practice.getPraImg());
        }
        practiceUpVO.setId(practice.getId())
                .setTeaId(practice.getTeaId())
                .setTeaName(teaName)
                .setCouName(couName.get(0))
                .setPraName(practice.getPraName())
                .setPraType(praType)
                .setPraSite(practice.getPraSite())
                .setPraMain(practice.getPraMain())
                .setPraImgName(practice.getPraImg())
                .setCreateTime(practice.getCreateTime())
                .setEndTime(new Date(practice.getCreateTime().getTime()+(1000*60*60*24*14)))
                .setGoodNumber(practice.getGoodNumber())
                .setJoinNumber(joinNumber)
                .setCompleteNumber(completeNumber);
        return practiceUpVO;
    }

    @Override
    public List<TypeVO> getAllPracticeType() {
        return practiceMapper.getAllPracticeType();
    }

    @Override
    public List<CourseAddVO> getAllCourseByTeaId(Long teaId) {
        return practiceMapper.getAllCourseByTeaId(teaId);
    }

    @Override
    public List<PracticeUpVO> getPracticeByInfo(String info) {
        List<PracticeUpVO> allPracticeVO = practiceMapper.getPracticeByInfo(info);
        if (allPracticeVO.size() != 0){
            for(PracticeUpVO practiceUpVO:allPracticeVO){
                Integer joinNumber = practiceMapper.getPraJoinNumberByPraId(practiceUpVO.getPraId());
                Integer completeNumber = practiceMapper.getPraCompleteNumberByPraId(practiceUpVO.getPraId());
                if (!practiceUpVO.getPraImgName().equals("default")){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath + fileSonPath+"\\"+practiceUpVO.getPraImgName());
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    practiceUpVO.setPraImgIO("data:image/jpeg;base64,"+encoder.encode(data));
                    practiceUpVO.setPraImgIO(practiceUpVO.getPraImgName());
                }
                practiceUpVO.setJoinNumber(joinNumber)
                        .setCompleteNumber(completeNumber)
                        .setEndTime(new Date(practiceUpVO.getCreateTime().getTime()+(1000*60*60*24*14)));
            }
        }else {
            return null;
        }
        return allPracticeVO;
    }

    // 同步处理Echarts数据，新写一个VO
    @Override
    public List<PracticeEchartsVO> getOverduePractice() {
        List<PracticeUpVO> allPracticeVO = practiceMapper.getOverduePractice();
        List<PracticeEchartsVO> practiceEchartsVOList = new ArrayList<>();
        if (allPracticeVO.size() != 0){
            for(PracticeUpVO practiceUpVO:allPracticeVO){
                Integer joinNumber = practiceMapper.getPraJoinNumberByPraId(practiceUpVO.getPraId());
                Integer completeNumber = practiceMapper.getPraCompleteNumberByPraId(practiceUpVO.getPraId());
                if (!practiceUpVO.getPraImgName().equals("default")){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath + fileSonPath+"\\"+practiceUpVO.getPraImgName());
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    practiceUpVO.setPraImgIO("data:image/jpeg;base64,"+encoder.encode(data));
                    practiceUpVO.setPraImgIO(practiceUpVO.getPraImgName());
                }
                practiceUpVO.setJoinNumber(joinNumber)
                        .setCompleteNumber(completeNumber)
                        .setEndTime(new Date(practiceUpVO.getCreateTime().getTime()+(1000*60*60*24*14)));
                List<String> classNames = practiceMapper.getPraClass(practiceUpVO.getPraId());
                List<String> newList = classNames.stream().distinct().collect(Collectors.toList());    // 去重
                List<PraClassVO> praClassVOList = new ArrayList<>();
                for (String name:newList){
                    Integer value = 0;
                    for (String className:classNames){
                        if (className.equals(name)){
                            value++;
                        }
                    }
                    PraClassVO praClassVO = new PraClassVO();
                    praClassVO.setName(name)
                            .setValue(value);
                    praClassVOList.add(praClassVO);
                }
                // 使用 Stream 流排序：由大到小
                praClassVOList = praClassVOList.stream().sorted(Comparator.comparing(PraClassVO::getValue).reversed())
                        .collect(Collectors.toList());
                List<Map> beforeAndAfterDateData = practiceMapper.getBeforeAndAfterDateData(practiceUpVO.getPraId());
                List<String> beforeDate = new ArrayList<>();
                List<String> afterDate = new ArrayList<>();
                List<Long> beforeData = new ArrayList<>();
                List<Long> afterData = new ArrayList<>();
                for (int i=0;i<beforeAndAfterDateData.size();i++){
                    if (i<7){
                        beforeDate.add((String) beforeAndAfterDateData.get(i).get("create_time"));
                        beforeData.add((Long) beforeAndAfterDateData.get(i).get("activity"));
                    }else {
                        afterDate.add((String) beforeAndAfterDateData.get(i).get("create_time"));
                        afterData.add((Long) beforeAndAfterDateData.get(i).get("activity"));
                    }
                }
                PracticeEchartsVO practiceEchartsVO = new PracticeEchartsVO();
                practiceEchartsVO.setPractice(practiceUpVO)
                        .setDate(practiceUpVO.getEndTime())
                        .setName(practiceUpVO.getPraName())
                        .setClassNumber(praClassVOList)
                        .setBeforeDate(beforeDate)
                        .setAfterDate(afterDate)
                        .setBeforeData(beforeData)
                        .setAfterData(afterData);
                practiceEchartsVOList.add(practiceEchartsVO);
            }
        }else {
            return null;
        }
        return practiceEchartsVOList;
    }
}
