package com.wisdom.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.base.util.*;
import com.wisdom.chapter.bean.vo.ChapVO;
import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.course.bean.dto.*;
import com.wisdom.course.bean.vo.*;
import com.wisdom.course.domain.Course;
import com.wisdom.course.mapper.CourseMapper;
import com.wisdom.course.service.CourseService;
import com.wisdom.type.bean.vo.TypeVO;
import com.wisdom.type.domain.Type;
import com.wisdom.user.bean.vo.StudentDetailedVO;
import com.wisdom.user.feign.RemoteUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import top.banner.lib.lock.RedisLock;
import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Service
@Transactional
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private QiniuService qiniuService;
    @Value("${APP_ID}")
    public String APP_ID;
    @Value("${API_KEY}")
    public String API_KEY;
    @Value("${SECRET_KEY}")
    public String SECRET_KEY;
    @Value("${score}")
    public int score;
    @Value("${clickScore}")
    public int clickScore;
    @Value("${scoreType}")
    public int scoreType;
    @Value("${clickType}")
    public int clickType;

    @Value("${filesonpath}")
    private String fileSonPath;
    private String filePath;
    public CourseServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }

    private Integer getVersion(Long id){
        return courseMapper.getVersion(id);
    }

    private List<Long> couTypeIds;

    @SneakyThrows
    @Override
    public R courseFile(MultipartFile multipartFile){
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
        File savePathFile = new File(filePath+fileSonPath + filename);
        if(!savePathFile.exists()){
            //若不存在该目录，则创建目录
            savePathFile.mkdirs();
        }
        multipartFile.transferTo(savePathFile);
        return R.data(filename, "图片上传成功");
    }

    public List<Type> getAllType(){
        return courseMapper.getAllType();
    }

    @Override
    public R addCourse(CourseAddDTO courseAddDTO) {
        Course newCourse = new Course();

        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);
        //图像初审
        int conclusionTypeM = Sample.sampleImage(sampleAip.getAip(), courseAddDTO.getCouPic());
        //文本初审
        int conclusionTypeT = Sample.sampleText(sampleAip.getAip(), courseAddDTO.getCouIntroduction());

        if (conclusionTypeT != 1 && conclusionTypeM != 1) {
            return R.error("内容，图像存在违规");
        }else if (conclusionTypeT != 1){
            return R.error("内容存在违规");
        }else if (conclusionTypeM != 1) {
            return R.error("图像存在违规");
        }

        newCourse.setCouId(courseAddDTO.getCouId())
                .setTeaId(courseAddDTO.getTeaId())
                .setCouName(courseAddDTO.getCouName())
                .setCouIntroduction(courseAddDTO.getCouIntroduction())
                .setCouPrice(courseAddDTO.getCouPrice())
                .setCouType(courseAddDTO.getCouType())
                .setCouPic(courseAddDTO.getCouPic());
        CourseAddVO courseAddVO = new CourseAddVO();
        CouAndTypeDTO couAndTypeDTO = new CouAndTypeDTO();
        couAndTypeDTO.setCouTypeId(courseAddDTO.getCouTypeId())
                .setCouId(courseAddDTO.getCouId());
        if(save(newCourse) && addCourseAndType(couAndTypeDTO)){
            courseAddVO.setCouId(newCourse.getId())
                    .setCouName(courseAddDTO.getCouName())
                    .setCouPrice(courseAddDTO.getCouPrice())
                    .setCouType(courseAddDTO.getCouType());
            return  R.data(courseAddVO, "新增课程成功");
        }else {
            return  R.error("新增课程失败");
        }
    }

    public Boolean addCourseAndType(CouAndTypeDTO couAndTypeDTO) {
        if (courseMapper.addCourseAndType(couAndTypeDTO) != null){
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteCourseById(Long id){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        boolean remove = remove(queryWrapper);
        return remove;
    }

    @SneakyThrows
    @Override
    public R updateCourseById(CourseUpdateDTO courseUpdateDTO) {
        if (courseUpdateDTO.getNewCouPic() != null){
            if (!courseUpdateDTO.getOldCouPic().equals("http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png")){
                String[] path = courseUpdateDTO.getOldCouPic().split("/");
                String key = path[3];
                try {
                    qiniuService.deleteFile(key);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
//            String filename = courseUpdateDTO.getOldCouPic();
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
        }
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",courseUpdateDTO.getId());
        Course newCourse = new Course();
        newCourse.setCouName(courseUpdateDTO.getCouName())
                .setCouIntroduction(courseUpdateDTO.getCouIntroduction())
                .setCouPrice(courseUpdateDTO.getCouPrice())
                .setCouType(courseUpdateDTO.getCouType())
                .setCouPic(courseUpdateDTO.getNewCouPic());
        CouUpTypeDTO couUpTypeDTO = new CouUpTypeDTO();
        couUpTypeDTO.setCouId(courseUpdateDTO.getCouId())
                .setCouTypeId(courseUpdateDTO.getCouTypeId());
        if (update(newCourse, queryWrapper) && upCourseAndType(couUpTypeDTO)){
            return R.ok("修改课程信息成功");
        }else {
            return R.error("修改课程信息失败");
        }
    }

    public Boolean upCourseAndType(CouUpTypeDTO couUpTypeDTO) {
        return courseMapper.upCourseAndType(couUpTypeDTO);
    }

    @Override
    public R teaQueryAllCourse(Long teaId) {
        CouTypeDetailedVO couTypeDetailedVO = new CouTypeDetailedVO();
        List<CourseDetailedVO> courseDetailedVOS = courseMapper.teaQueryAllCourse(teaId);
        if (courseDetailedVOS != null){
            for (CourseDetailedVO courseDetailedVO:courseDetailedVOS){
                String couPic = courseDetailedVO.getCouPic();
//                if (couPic != null && couPic != ""){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath + fileSonPath+"\\"+couPic);
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    courseDetailedVO.setCouPicIO("data:image/jpeg;base64,"+encoder.encode(data));
//                }
                courseDetailedVO.setCouPicIO(couPic);
            }
        }
        List<Type> types = getAllType();
        List<Object> typeLOF = new ArrayList<>();
        Map<String,Integer> typeMIS = new HashMap<>();
        for (Type type:types){
            Map<String,Object> typeMSO = new HashMap<>();
            if (type.getParentTypeId() == null){
                typeMSO.put("value",type.getTypeName());
                typeMSO.put("typeId",Long.toString(type.getCouTypeId()));
                typeLOF.add(typeMSO);
                typeMIS.put(Long.toString(type.getCouTypeId()),typeLOF.size());
            }
        }
        for (Type type:types){
            if (type.getParentTypeId() != null){
                Integer index = typeMIS.get(Long.toString(type.getParentTypeId()));
                Map<String,Object> typeMap = (Map<String, Object>) typeLOF.get(index-1);
                if (typeMap.size() == 2){
                    List<Object> typeLOZ = new ArrayList<>();
                    Map<String,Object> typeMSO = new HashMap<>();
                    typeMSO.put("value",type.getTypeName());
                    typeMSO.put("typeId",Long.toString(type.getCouTypeId()));
                    typeLOZ.add(typeMSO);
                    typeMap.put("ch_type",typeLOZ);
                }else {
                    List<Object> typeLOZ = (List<Object>) typeMap.get("ch_type");
                    Map<String,Object> typeMSO = new HashMap<>();
                    typeMSO.put("value",type.getTypeName());
                    typeMSO.put("typeId",Long.toString(type.getCouTypeId()));
                    typeLOZ.add(typeMSO);
                    typeMap.remove("ch_type");
                    typeMap.put("ch_type", typeLOZ);
                }
                typeLOF.set(index-1,typeMap);
            }
        }
//        Map<String,Object> typeMap = (Map<String, Object>)typeLOF.get(1);
//        List<Object> list = (List<Object>) typeMap.get("ch_type");
//        Map<String,String> map = (Map<String, String>) list.get(0);
//        System.out.println(map.get("typeId"));
//        System.out.println(map.get("typeId").getClass().getTypeName());
        couTypeDetailedVO.setTypeList1(typeLOF)
                .setCourseDetailedVOS(courseDetailedVOS);
        return R.data(couTypeDetailedVO, "查询教师所有课程成功");
    }

    @Override
    public List<CourseAboutVO> queryCourseByClassId(Long classId) {
        List<CourseAboutVO> courseAboutVOS = courseMapper.queryCourseByClassId(classId);
        if (courseAboutVOS != null){
            for (CourseAboutVO courseAboutVO:courseAboutVOS){
                String couPic = courseAboutVO.getCouPic();
//                if (couPic != null && couPic != ""){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath+fileSonPath+"\\"+couPic);
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    courseAboutVO.setCouPicIO("data:image/jpeg;base64,"+encoder.encode(data));
//                }
                courseAboutVO.setCouPicIO(couPic);
            }
        }
        return courseAboutVOS;
    }

    @Override
    public List<CourseVO> getCourseByCouId(List<Long> couId) {
        List<CourseVO> courseVOList = courseMapper.getCourseByCouId(couId);
        for (CourseVO courseVO:courseVOList){
            String couPic = courseVO.getCouPic();
            courseVO.setTeaName(remoteUserService.getTeaNameByTeaId(courseVO.getTeaId()));
//            if (couPic != null && couPic != ""){
//                InputStream in = null;
//                byte[] data = null;
//                try {
//                    in = new FileInputStream(filePath + fileSonPath+"\\"+couPic);
//                    data = new byte[in.available()];
//                    in.read(data);
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //进行Base64编码
//                BASE64Encoder encoder = new BASE64Encoder();
//                courseVO.setCouPicIO("data:image/jpeg;base64,"+encoder.encode(data));
//            }
            courseVO.setCouPicIO(couPic);
        }
        return courseVOList;
    }

    @Override
    public List<Integer> getCourPriceByCouId(List<Long> couId) {
        return courseMapper.getCourPriceByCouId(couId);
    }

    @Override
    public CourseVO courseDetailByCouId(Long couId) {
        CourseVO courseVO = courseMapper.courseDetailByCouId(couId);
        if (courseVO != null){
            String couPic = courseVO.getCouPic();
            courseVO.setTeaName(remoteUserService.getTeaNameByTeaId(courseVO.getTeaId()));
//            if (couPic != null && couPic != ""){
//                InputStream in = null;
//                byte[] data = null;
//                try {
//                    in = new FileInputStream(filePath+fileSonPath+"\\"+couPic);
//                    data = new byte[in.available()];
//                    in.read(data);
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //进行Base64编码
//                BASE64Encoder encoder = new BASE64Encoder();
//                courseVO.setCouPicIO("data:image/jpeg;base64,"+encoder.encode(data));
//            }
            courseVO.setCouPicIO(couPic);
        }
        return courseVO;
    }

    @Override
    public List<Long> getChaIdsByCouId(Long couId) {
        return courseMapper.getChaIdsByCouId(couId);
    }

    @Override
    public List<CourseAboutVO> getCourseByStuId(Long stuId) {
        List<CourseAboutVO> courseAboutVOS = courseMapper.getCourseByStuId(stuId);
        if (courseAboutVOS.size() != 0){
            for (CourseAboutVO courseAboutVO:courseAboutVOS){
                List<Long> couProgressTotal = getStuCouProgress(stuId, courseAboutVO.getCouId());
                Long datas = couProgressTotal.get(0)+couProgressTotal.get(1);
                Long couProgressBfb = null;
                if (couProgressTotal.get(2) != 0L){
                    couProgressBfb = datas*100/couProgressTotal.get(2);
                    Math.ceil(couProgressBfb);
                }else {
                    couProgressBfb = 0L;
                }
                String couPic = courseAboutVO.getCouPic();
//                if (couPic != null && couPic != ""){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath + fileSonPath+"\\"+couPic);
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
//                    courseAboutVO.setCouPicIO("data:image/jpeg;base64,"+encoder.encode(data))
//                            .setCouProgress(couProgressTotal.get(0))
//                            .setCouTotalProgress(couProgressTotal.get(1))
//                            .setCouProgressBfb(couProgressBfb);
//                }
                courseAboutVO.setCouPicIO(couPic)
                        .setCouProgress(couProgressTotal.get(0))
                        .setCouTotalProgress(couProgressTotal.get(1))
                        .setCouProgressBfb(couProgressBfb);
            }
        }
        return courseAboutVOS;
    }

    @Override
    public List<CouAboutClassVO> classQueryAllCourse() {
        List<CouAboutClassVO> couAboutClassVOS = courseMapper.classQueryAllCourse();
        for(CouAboutClassVO couAboutClassVO:couAboutClassVOS){
            Long teaId = couAboutClassVO.getTeaId();
            List<Long> couProgress = courseMapper.getCouProgress(couAboutClassVO.getCouId());
            Long couProgressTotal = 0L;
            for (Long total:couProgress){
                couProgressTotal += total;
            }
            String couFirstCha = courseMapper.getCouFirstChaURL(couAboutClassVO.getCouId());
            String couPic = couAboutClassVO.getCouPic();
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath+fileSonPath+"\\"+couPic);
//                data = new byte[in.available()];
//                in.read(data);
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //进行Base64编码
//            BASE64Encoder encoder = new BASE64Encoder();
//            couAboutClassVO.setCouFirstCha(couFirstCha)
//                    .setCouProgressTotal(couProgressTotal)
//                    .setCouPic("data:image/jpeg;base64,"+encoder.encode(data))
//                    .setTeaName(remoteUserService.getTeaNameByTeaId(teaId))
//                    .setTeaId(-1L);
            couAboutClassVO.setCouFirstCha(couFirstCha)
                    .setCouProgressTotal(couProgressTotal)
                    .setCouPic(couPic)
                    .setTeaName(remoteUserService.getTeaNameByTeaId(teaId))
                    .setTeaId(-1L);
        }
        return couAboutClassVOS;
    }

    @Override
    public Boolean updateCouCataNumById(Long id){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course newCourse = new Course();
        Course course= getOne(queryWrapper.eq("id",id));
        Integer couCataNum = course.getCouCataNum( );
        newCourse.setCouCataNum(couCataNum + 1)
                .setVersion(getVersion(id));
        return update(newCourse,queryWrapper);
    }

    @Override
    public Boolean updateCouCollNumById(CouInteractionUpDTO couInteractionUpDTO){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course newCourse = new Course();
        CouAllNumVO couAllNumVO = courseMapper.getCouAllNum(couInteractionUpDTO.getCouId());
        queryWrapper.eq("id", couAllNumVO.getId());
        Long couCollNum = couAllNumVO.getCouCollNum( );
        CourseInteractionVO courseInteractionVO = courseMapper.getCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
        Integer isCol = null;
        if (courseInteractionVO == null){
            Boolean success = courseMapper.addCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
            if (success == true){
                isCol = 0;
            }
        }else {
            isCol = courseInteractionVO.getCollect();
        }
        Boolean isYes = true;
        if (isCol == 0){
            couInteractionUpDTO.setCollect(1);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setCouCollNum(couCollNum + 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }else {
            couInteractionUpDTO.setCollect(0);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setCouCollNum(couCollNum - 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }
        if (isYes == true){
            return update(newCourse,queryWrapper);
        }else {
            return false;
        }
    }

    @Override
    public Boolean updateCartState(CouInteractionUpDTO couInteractionUpDTO){
        CourseInteractionVO courseInteractionVO = courseMapper.getCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
        Integer isCart = null;
        if (courseInteractionVO == null){
            Boolean success = courseMapper.addCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
            if (success == true){
                isCart = 0;
            }
        }else {
            isCart = courseInteractionVO.getCart();
        }
        if (isCart == 0){
            couInteractionUpDTO.setCart(1);
            return courseMapper.updateCouInteraction(couInteractionUpDTO);
        }else {
            couInteractionUpDTO.setCart(0);
            return courseMapper.updateCouInteraction(couInteractionUpDTO);
        }
    }

    @Override
    public Boolean updateCouShareNumById(CouInteractionUpDTO couInteractionUpDTO){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course newCourse = new Course();
        CouAllNumVO couAllNumVO = courseMapper.getCouAllNum(couInteractionUpDTO.getCouId());
        queryWrapper.eq("id", couAllNumVO.getId());
        Long couShareNum = couAllNumVO.getCouShareNum( );
        CourseInteractionVO courseInteractionVO = courseMapper.getCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
        Integer isShare = null;
        if (courseInteractionVO == null){
            Boolean success = courseMapper.addCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
            if (success == true){
                isShare = 0;
            }
        }else {
            isShare = courseInteractionVO.getShare();
        }
        Boolean isYes = true;
        if (isShare == 0){
            couInteractionUpDTO.setShare(1);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setCouShareNum(couShareNum + 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }else {
            isYes = false;
        }
        if (isYes == true){
            return update(newCourse,queryWrapper);
        }else {
            return false;
        }
    }

    @Override
    public Boolean updateCouClicksById(Long id){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course newCourse = new Course();
        Course course = getOne(queryWrapper.eq("id", id));
        Long clicks = course.getClicks( );
        newCourse.setClicks(clicks + 1)
                .setVersion(getVersion(id));
        return update(newCourse,queryWrapper);
    }

    @Override
    public Boolean updateCouRecommendNumById(CouInteractionUpDTO couInteractionUpDTO){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course newCourse = new Course();
        CouAllNumVO couAllNumVO = courseMapper.getCouAllNum(couInteractionUpDTO.getCouId());
        queryWrapper.eq("id", couAllNumVO.getId());
        Long recommendNum = couAllNumVO.getRecommendNum( );
        CourseInteractionVO courseInteractionVO = courseMapper.getCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
        Integer isRecommend = null;
        if (courseInteractionVO == null){
            Boolean success = courseMapper.addCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
            if (success == true){
                isRecommend = 0;
            }
        }else {
            isRecommend = courseInteractionVO.getRecommend();
        }
        Boolean isYes = true;
        if (isRecommend == 0){
            couInteractionUpDTO.setRecommend(1);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setRecommendNum(recommendNum + 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }else {
            couInteractionUpDTO.setRecommend(0);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setRecommendNum(recommendNum - 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }
        if (isYes == true){
            return update(newCourse,queryWrapper);
        }else {
            return false;
        }
    }

    @Override
    public Boolean updateCouPurchaseById(CouInteractionUpDTO couInteractionUpDTO){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course newCourse = new Course();
        CouAllNumVO couAllNumVO = courseMapper.getCouAllNum(couInteractionUpDTO.getCouId());
        queryWrapper.eq("id", couAllNumVO.getId());
        Long purchaseNum = couAllNumVO.getPurchaseNum( );
        CourseInteractionVO courseInteractionVO = courseMapper.getCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
        Integer isPurchase = null;
        if (courseInteractionVO == null){
            Boolean success = courseMapper.addCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
            if (success == true){
                isPurchase = 0;
            }
        }else {
            isPurchase = courseInteractionVO.getHave();
        }
        Boolean isYes = true;
        if (isPurchase == 0){
            couInteractionUpDTO.setHave(1);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setPurchaseNum(purchaseNum + 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }else {
            couInteractionUpDTO.setHave(0);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setPurchaseNum(purchaseNum - 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }
        if (isYes == true){
            return update(newCourse,queryWrapper);
        }else {
            return false;
        }
    }

    @Override
    public Boolean isCouPurchaseById(CouInteractionUpDTO couInteractionUpDTO){
        CourseInteractionVO courseInteractionVO = courseMapper.getCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
        Integer isHave = null;
        if (courseInteractionVO == null){
            return false;
        }else {
            isHave = courseInteractionVO.getHave();
        }
        if (isHave == 0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public List<CourseBriefVO> selectCourseByClassId(Long classId) {
        List<CourseBriefVO> courseBriefVOList = courseMapper.selectCourseByClassId(classId);
        if (courseBriefVOList.size() != 0){
            for (CourseBriefVO courseBriefVO:courseBriefVOList){
                String couPic = courseBriefVO.getCouPic();
//                InputStream in = null;
//                byte[] data = null;
//                try {
//                    in = new FileInputStream(filePath + fileSonPath+"\\"+couPic);
//                    data = new byte[in.available()];
//                    in.read(data);
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //进行Base64编码
//                BASE64Encoder encoder = new BASE64Encoder();
//                courseBriefVO.setCouPicIO("data:image/jpeg;base64,"+encoder.encode(data));
                courseBriefVO.setCouPicIO(couPic);
            }
        }
        return courseBriefVOList;
    }

    @Override
    public List<CourseBriefVO> selectAllCourse(Integer startIndex, Integer pageSize, Long classId) {
        List<CourseBriefVO> courseBriefVOList = courseMapper.selectAllCourse(startIndex*pageSize, pageSize);
        List<Long> couId = courseMapper.selectCourseIdByClassId(classId);
        if (courseBriefVOList.size() != 0){
            for (int i=0;i<courseBriefVOList.size();i++){
                    Boolean isContinue = false;
                    for (Long cId:couId){
                        if (cId.toString().equals(courseBriefVOList.get(i).getCouId().toString())){
                            courseBriefVOList.get(i).setState(1);
                            break;
                        }
                    }
                    String couPic = courseBriefVOList.get(i).getCouPic();
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath + fileSonPath+"\\"+couPic);
//                data = new byte[in.available()];
//                in.read(data);
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //进行Base64编码
//            BASE64Encoder encoder = new BASE64Encoder();
//            courseBriefVOList.get(i).setCouPicIO("data:image/jpeg;base64,"+encoder.encode(data));
                    courseBriefVOList.get(i).setCouPicIO(couPic);

            }
        }

        return courseBriefVOList;
    }

    @Override
    public List<CourseBriefVO> selectAllCourseComplete() {
        List<CourseBriefVO> courseBriefVOList = courseMapper.selectAllCourseComplete();
        if (courseBriefVOList.size() != 0){
            for (CourseBriefVO courseBriefVO:courseBriefVOList){
                String couPic = courseBriefVO.getCouPic();
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath + fileSonPath+"\\"+couPic);
//                data = new byte[in.available()];
//                in.read(data);
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //进行Base64编码
//            BASE64Encoder encoder = new BASE64Encoder();
//            courseBriefVO.setCouPicIO("data:image/jpeg;base64,"+encoder.encode(data));
                courseBriefVO.setCouPicIO(couPic);
            }
        }
        return courseBriefVOList;
    }

    @Override
    public Boolean updateEvaluateNumById(CouInteractionUpDTO couInteractionUpDTO){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course newCourse = new Course();
        CouAllNumVO couAllNumVO = courseMapper.getCouAllNum(couInteractionUpDTO.getCouId());
        queryWrapper.eq("id", couAllNumVO.getId());
        Long evaluateNum = couAllNumVO.getEvaluateNum( );
        CourseInteractionVO courseInteractionVO = courseMapper.getCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
        Integer isEvaluate = null;
        if (courseInteractionVO == null){
            Boolean success = courseMapper.addCourseInteraction(couInteractionUpDTO.getCouId(), couInteractionUpDTO.getStuId());
            if (success == true){
                isEvaluate = 0;
            }
        }else {
            isEvaluate = courseInteractionVO.getEvaluate();
        }
        Boolean isYes = true;
        if (isEvaluate == 0){
            couInteractionUpDTO.setEvaluate(1);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setEvaluateNum(evaluateNum + 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }else {
            couInteractionUpDTO.setEvaluate(0);
            Boolean pass = courseMapper.updateCouInteraction(couInteractionUpDTO);
            if (pass == true){
                newCourse.setEvaluateNum(evaluateNum - 1)
                        .setVersion(getVersion(couAllNumVO.getId()));
            }else {
                isYes = false;
            }
        }
        if (isYes == true){
            return update(newCourse,queryWrapper);
        }else {
            return false;
        }
    }

    public List<Long> getStuCouProgress(Long stuId, Long couId){
        CouProgressDTO couProgressDTO = new CouProgressDTO();
        couProgressDTO.setCouId(couId)
                .setStuId(stuId);
        List<Long> couProgress = courseMapper.getStuCouProgress(couProgressDTO);
        List<Long> classCouProgress = courseMapper.getStuClassCouProgress(couProgressDTO);
        Long couTotal = 0L;
        if (couProgress.size() != 0){
            List<Long> totals = courseMapper.getCouProgress(couProgressDTO.getCouId());
            for (Long total:totals){
                couTotal += total;
            }
        }else {
            List<Long> totals = courseMapper.getCouProgress(couProgressDTO.getCouId());
            for (Long total:totals){
                couTotal += total;
            }
        }
        Long endTimeTotal = 0L;
        Long classEndTimeTotal = 0L;
        List<Long> couProgressTotal = new ArrayList<>();
        if (couProgress.size() != 0){
            for (Long endTime:couProgress){
                endTimeTotal += endTime;
            }
            couProgressTotal.add(endTimeTotal);
        }else {
            couProgressTotal.add(0L);
        }
        if (classCouProgress.size() != 0){
            for (Long endTime:classCouProgress){
                classEndTimeTotal += endTime;
            }
            couProgressTotal.add(classEndTimeTotal);
        }else {
            couProgressTotal.add(0L);
        }
        couProgressTotal.add(couTotal);
        return couProgressTotal;
    }

    @Override
    public List<CourseVO> recommendCourse(Long stuId) {
        List<CourseVO> courseVOList = new ArrayList<>();
        //收藏课程
        List<Long> longs = courseMapper.collectionType(stuId);
        //购物车
        List<Long> longs1 = courseMapper.preType(stuId);
        //班级课程
        List<Long> longs2 = courseMapper.clasCouType(stuId);
        //自选课程
        List<Long> longs3 = courseMapper.couType(stuId);
        if (longs1 != null) {
            longs.addAll(longs1);
        }
        if (longs2 != null) {
            longs.addAll(longs2);
        }
        if (longs3 != null) {
            longs.addAll(longs3);
        }
        if (stuId == null) {
            courseVOList =  courseMapper.maxClickCourse(6);
            for (int i=0;i<courseVOList.size();i++) {
                courseVOList.get(i).setCouPic(courseVOList.get(i).getCouPic());
            }
            return courseVOList;
        }
        couTypeIds = longs;
        if(longs.size() != 0) {
            courseVOList = courseMapper.recommendCourse(longs);
        }

        for (int i=0;i<courseVOList.size();i++) {
            courseVOList.get(i).setCouPic(courseVOList.get(i).getCouPic());
        }
        int num = 6-courseVOList.size();
        if (num>0) {
            List<CourseVO> courseVOList1 = courseMapper.maxClickCourse(num);
            for (int i=0;i<courseVOList1.size();i++) {
                courseVOList1.get(i).setCouPic(courseVOList1.get(i).getCouPic());
            }
            courseVOList.addAll(courseVOList1);
        }
        return courseVOList;
    }

    @Override
    public List<TypeVO> recommendType() {
        if (couTypeIds == null || couTypeIds.size() == 0) {
            List<TypeVO> typeVOS = courseMapper.maxClickType(3);
            return typeVOS;
        }

        List<TypeVO> typeVOS = courseMapper.recommendType(couTypeIds);
        int num = 3-typeVOS.size();
        if (num>0) {
            List<TypeVO> typeVOS1 = courseMapper.maxClickType(num);
            typeVOS.addAll(typeVOS1);
        }
        return typeVOS;
    }

    @Override
    public Integer getCouPrice(Long couId) {
        return courseMapper.getCouPrice(couId);
    }

    @Override
    public List<String> selectCourseName(List<Long> couIds) {
        List<String> couNames = new ArrayList<>();
        for (Long couId:couIds){
            String couName = courseMapper.selectCourseName(couId);
            couNames.add(couName);
        }
        return couNames;
    }

    @Override
    public R getAllAnyFieryCourse(){
        List<AnyFieryCourseVO> anyFieryCourseVOList = courseMapper.getAllAnyFieryCourse();
        if (anyFieryCourseVOList.size() != 0){
            AnyFieryCourseVO anyFieryCourseVOS[] = new AnyFieryCourseVO[anyFieryCourseVOList.size()];
            List<SimpleAnyFieryCourseVO> simpleAnyFieryCourseVOList = new ArrayList<>();
            int index = 0;
            for (AnyFieryCourseVO allAnyFieryCourseVO:anyFieryCourseVOList){
                Integer score = allAnyFieryCourseVO.getClicks()+
                        allAnyFieryCourseVO.getCouShareNum()*4+
                        allAnyFieryCourseVO.getCouCollNum()*4+
                        allAnyFieryCourseVO.getEvaluateNum()*3+
                        allAnyFieryCourseVO.getRecommendNum()*4+
                        allAnyFieryCourseVO.getCouPrice()*2+
                        allAnyFieryCourseVO.getPurchaseNum()*5;
                allAnyFieryCourseVO.setScore(score);
                anyFieryCourseVOS[index] = allAnyFieryCourseVO;
                index++;
            }
            for (int i = 0; i <= anyFieryCourseVOS.length - 2; i++) {
                for (int j = i + 1; j <= anyFieryCourseVOS.length - 1; j++) {
                    if (anyFieryCourseVOS[i].getScore() < anyFieryCourseVOS[j].getScore()) {
                        AnyFieryCourseVO temp = anyFieryCourseVOS[i];
                        anyFieryCourseVOS[i] = anyFieryCourseVOS[j];
                        anyFieryCourseVOS[j] = temp;
                    }
                }
            }
            for (AnyFieryCourseVO a:anyFieryCourseVOS){
                SimpleAnyFieryCourseVO simpleAnyFieryCourseVO = new SimpleAnyFieryCourseVO();

                String couPic = a.getCouPic();
//                InputStream in = null;
//                byte[] data = null;
//                try {
//                    in = new FileInputStream(filePath + fileSonPath+"\\"+couPic);
//                    data = new byte[in.available()];
//                    in.read(data);
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(a.getCreateTime().getTime());
                //进行Base64编码
//                BASE64Encoder encoder = new BASE64Encoder();
                simpleAnyFieryCourseVO.setCouId(a.getCouId())
                        .setCouName(a.getCouName())
                        .setTeaName(a.getTeaName())
                        .setCouPicIO(couPic)
                        .setCreateTime(formatter.format(date));
                simpleAnyFieryCourseVOList.add(simpleAnyFieryCourseVO);
            }
            List<SimpleAnyFieryCourseVO> carouselCourseList = new ArrayList<>();
            List<SimpleAnyFieryCourseVO> firstCourseList = new ArrayList<>();
            List<SimpleAnyFieryCourseVO> secondCourseList = new ArrayList<>();
            List<List<SimpleAnyFieryCourseVO>> thirdCourseList = new ArrayList<>();
            List<SimpleAnyFieryCourseVO> thirdCourseS = new ArrayList<>();
            for (int i=0;i<simpleAnyFieryCourseVOList.size();i++){
                if (i<6){
                    carouselCourseList.add(simpleAnyFieryCourseVOList.get(i));
                }else if (i>5 && i<12){
                    firstCourseList.add(simpleAnyFieryCourseVOList.get(i));
                }else if (i>11 && i<20){
                    secondCourseList.add(simpleAnyFieryCourseVOList.get(i));
                }else if (i>19 && i<24){
                    thirdCourseList.add(thirdCourseS);
                }
            }
            for (int i=20;i<simpleAnyFieryCourseVOList.size();i++){
                if (i<28){
                    thirdCourseList.get(0).add(simpleAnyFieryCourseVOList.get(i));
                }else if (i>27 && i<36){
                    thirdCourseList.get(1).add(simpleAnyFieryCourseVOList.get(i));
                }else if (i>35 && i<44){
                    thirdCourseList.get(2).add(simpleAnyFieryCourseVOList.get(i));
                }else if (i<52){
                    thirdCourseList.get(3).add(simpleAnyFieryCourseVOList.get(i));
                }
            }
            AllAnyFieryCourseVO allAnyFieryCourseVO = new AllAnyFieryCourseVO();
            allAnyFieryCourseVO.setCarouselCourseList(carouselCourseList)
                    .setFirstCourseList(firstCourseList)
                    .setSecondCourseList(secondCourseList)
                    .setThirdCourseList(thirdCourseList);
            return R.data(allAnyFieryCourseVO);
        }else {
            return R.error("null");
        }
    }

    @Override
    public List<TypeVO> getParentTypeId() {
        return courseMapper.getParentTypeId();
    }

    @Override
    public List<TypeVO> getCouTypeByParentTypeId(Long parentTypeId) {
        return courseMapper.getCouTypeByParentTypeId(parentTypeId);
    }

    @Override
    public List<TypeVO> getFieryCouTypeId() {
        List<Map> maps = courseMapper.getFieryCouTypeId();
        Map<String,Integer> mapMain = new HashMap<>();
        for(Map map:maps){
            if (mapMain.get(map.get("cou_type_id")+" "+map.get("type_name")) == null){
                mapMain.put(map.get("cou_type_id")+" "+map.get("type_name"),1);
            }else {
                mapMain.put(map.get("cou_type_id")+" "+map.get("type_name"),mapMain.get(map.get("cou_type_id")+" "+map.get("type_name"))+1);
            }
        }
        Object[] objectArray = mapMain.entrySet().toArray();
        for (int i = 0; i <= objectArray.length - 2; i++) {
            for (int j = i + 1; j <= objectArray.length - 1; j++) {
                if (Integer.valueOf(String.valueOf(objectArray[i]).substring(String.valueOf(objectArray[i]).indexOf("=")+1)) < Integer.valueOf(String.valueOf(objectArray[j]).substring(String.valueOf(objectArray[j]).indexOf("=")+1))) {
                    String temp = String.valueOf(objectArray[i]);
                    objectArray[i] = objectArray[j];
                    objectArray[j] = temp;
                }
            }
        }
        List<TypeVO> typeVOList = new ArrayList<>();
        for (int i=0;i<6;i++){
            TypeVO typeVO = new TypeVO();
            typeVO.setCouTypeId(Long.valueOf(String.valueOf(objectArray[i]).substring(0,19)))
                    .setTypeName(String.valueOf(objectArray[i]).substring(20,String.valueOf(objectArray[i]).indexOf("=")));
            typeVOList.add(typeVO);
        }
        return typeVOList;
    }

    @Override
    public List<SimpleAnyFieryCourseVO> getCourseByInfo(String courseInfo) {
        List<SimpleAnyFieryCourseVO> simpleAnyFieryCourseVOList = courseMapper.getCourseByInfo(courseInfo);
        if (simpleAnyFieryCourseVOList.size() != 0){
            for(SimpleAnyFieryCourseVO simpleAnyFieryCourseVO:simpleAnyFieryCourseVOList){
                if (!simpleAnyFieryCourseVO.getCouPicName().equals("default")){
//                    InputStream in = null;
//                    byte[] data = null;
//                    try {
//                        in = new FileInputStream(filePath + fileSonPath+"\\"+simpleAnyFieryCourseVO.getCouPicName());
//                        data = new byte[in.available()];
//                        in.read(data);
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //进行Base64编码
//                    BASE64Encoder encoder = new BASE64Encoder();
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date(simpleAnyFieryCourseVO.getCreateTimeE().getTime());
                    simpleAnyFieryCourseVO.setCouPicIO(simpleAnyFieryCourseVO.getCouPicName())
                            .setCreateTime(formatter.format(date));
                }
            }
        }else {
            return null;
        }
        return simpleAnyFieryCourseVOList;
    }

    @Override
    public String getCouIntroduction(Long couId) {
        return courseMapper.getCouIntroduction(couId);
    }

    @Override
    public CourseDetailedVO courseDetail(Long stuId, Long couId) {
        CourseDetailedVO courseDetailedVO = courseMapper.courseDetail(couId);
        courseDetailedVO.setCouPic(courseDetailedVO.getCouPic());
        TypeVO typeVO = courseMapper.getType(couId);
        courseDetailedVO.setMaxTypeId(typeVO.getParentTypeId());
        courseDetailedVO.setMinTypeId(typeVO.getCouTypeId());
        courseDetailedVO.setMaxTypeName(courseMapper.getTypeName(typeVO.getParentTypeId()));
        courseDetailedVO.setMinTypeName(typeVO.getTypeName());
        if (stuId != null) {
            courseDetailedVO.setIsClick(courseMapper.isClick(stuId, couId));
            courseDetailedVO.setIsCollection(remoteUserService.isCollection(stuId, couId));
            courseDetailedVO.setIsAddCart(remoteUserService.isAddCart(stuId, couId));
            courseDetailedVO.setIsBuy(remoteUserService.isBuy(stuId, couId));
        }
        List<CouVO> relatedCourse = courseMapper.getRelatedCourse(courseDetailedVO.getCouId());
        for (int i=0;i<relatedCourse.size();i++) {
            relatedCourse.get(i).setCouPic(relatedCourse.get(i).getCouPic());
        }
        courseDetailedVO.setCouRelatedVOList(relatedCourse);
        return courseDetailedVO;
    }

    @Override
    public CourseDetailedVO courseDetail1(Long stuId, Long couId) {
        CourseDetailedVO courseDetailedVO = courseMapper.courseDetail(couId);
        //课程封面
        courseDetailedVO.setCouPic(courseDetailedVO.getCouPic());
        TypeVO typeVO = courseMapper.getType(couId);
        //课程大类
        courseDetailedVO.setMaxTypeId(typeVO.getParentTypeId());
        //课程小类
        courseDetailedVO.setMinTypeId(typeVO.getCouTypeId());
        courseDetailedVO.setMaxTypeName(courseMapper.getTypeName(typeVO.getParentTypeId()));
        courseDetailedVO.setMinTypeName(typeVO.getTypeName());
        return courseDetailedVO;
    }

    @Override
    public CourseDetailedVO courseDetail2(Long stuId, Long couId) {
        CourseDetailedVO courseDetailedVO = courseMapper.courseDetail(couId);
        List<CouVO> relatedCourse = courseMapper.getRelatedCourse(courseDetailedVO.getCouId());
        for (int i=0;i<relatedCourse.size();i++) {
            relatedCourse.get(i).setCouPic(relatedCourse.get(i).getCouPic());
        }
        courseDetailedVO.setCouRelatedVOList(relatedCourse);
        return courseDetailedVO;
    }

    @Override
    public CourseDetailedVO courseDetail3(Long stuId, Long couId) {
        CourseDetailedVO courseDetailedVO = courseMapper.courseDetail(couId);
        if (stuId != null) {
            courseDetailedVO.setIsClick(courseMapper.isClick(stuId, couId));
            courseDetailedVO.setIsCollection(remoteUserService.isCollection(stuId, couId));
            courseDetailedVO.setIsAddCart(remoteUserService.isAddCart(stuId, couId));
            courseDetailedVO.setIsBuy(remoteUserService.isBuy(stuId, couId));
        }else {
            courseDetailedVO.setIsClick(0);
            courseDetailedVO.setIsCollection(0);
            courseDetailedVO.setIsAddCart(0);
            courseDetailedVO.setIsBuy(0);
        }
        return courseDetailedVO;
    }

    @Override
    public List<ChapVO> allChapterByCouId(Long couId) {
        List<ChapVO> chapVOS = courseMapper.allChapterByCouId(couId);
        for (int i=0;i<chapVOS.size();i++) {
            Long chaTime = chapVOS.get(i).getChaTime();
            long l = chaTime / 60; //分钟数
            Long s = chaTime - l*60; //秒数
            Long h = l / 60; //小时数
            Long m = l - h*60; //分钟数
            String time = "";
            if (h != 0) {
                time = h.toString()+"小时";
            }
            if (m != 0) {
                time = m.toString()+"分钟";
            }
            if (s != 0) {
                time = s.toString()+"秒";
            }
            chapVOS.get(i).setTime(time);
        }
        return chapVOS;
    }

    @Override
    public RespPageBean search(String val, IPage<CouVO> page) {
        RespPageBean respPageBean = new RespPageBean();
        IPage<CouVO> search = courseMapper.search(val, page);
        List<CouVO> records = search.getRecords();
        for (int i=0;i<records.size();i++) {
            TypeVO typeVO = courseMapper.getType(records.get(i).getCouId());
            records.get(i).setMaxTypeId(typeVO.getParentTypeId());
            records.get(i).setMinTypeId(typeVO.getCouTypeId());
            records.get(i).setMaxTypeName(courseMapper.getTypeName(typeVO.getParentTypeId()));
            records.get(i).setMinTypeName(typeVO.getTypeName());
            records.get(i).setCouPic(records.get(i).getCouPic());
            records.get(i).setCouCataNum(courseMapper.getCataNumByCouId(records.get(i).getCouId()));
        }
        respPageBean.setTotal(search.getTotal());
        respPageBean.setData(records);
        respPageBean.setPages(search.getPages());
        return respPageBean;
    }

    @Override
    public List<ChapterVO> getClaCourseAllCha(Long couId) {
        return courseMapper.getClaCourseAllCha(couId);
    }

    @RedisLock(name = "couClick", key = "#p0 + ':' + #p1")
    @Override
    public ClickVO click(Long stuId, Long couId) {
        ClickVO clickVO = new ClickVO();
        Long recommendNum = courseMapper.getClicks(couId);
        courseMapper.addClick(stuId, couId);
        courseMapper.updateCouClick(couId, recommendNum + 1);
        clickVO.setRecommendNum(recommendNum + 1);
        clickVO.setIsClick(1);
        //点赞加积分
        //评价课程加积分(流水)
        courseMapper.addScore(stuId, clickType, clickScore);
        int stuScoreByStuId = remoteUserService.getStuScoreByStuId(stuId);
        remoteUserService.updateScore(stuId, stuScoreByStuId+clickScore);
        return clickVO;
    }

    @RedisLock(name = "couClick", key = "#p0 + ':' + #p1")
    @Override
    public ClickVO cancelClick(Long stuId, Long couId) {
        ClickVO clickVO = new ClickVO();
        Long recommendNum = courseMapper.getClicks(couId);
        courseMapper.deletedClick(stuId, couId);
        courseMapper.updateCouClick(couId, recommendNum - 1);
        clickVO.setRecommendNum(recommendNum - 1);
        clickVO.setIsClick(0);
        return clickVO;
    }

    @Override
    public RespPageBean seeMore(IPage<CouVO> page, Long maxTypeId, Long minTypeId, int couPrice, int clickNum) {
        RespPageBean respPageBean = new RespPageBean();
        IPage<CouVO> couVOIPage = courseMapper.seeMore(page, maxTypeId, minTypeId, couPrice, clickNum);
        List<CouVO> records = couVOIPage.getRecords();
        for (int i=0;i<records.size();i++) {
            TypeVO typeVO = courseMapper.getType(records.get(i).getCouId());
            records.get(i).setMaxTypeId(typeVO.getParentTypeId());
            records.get(i).setMinTypeId(typeVO.getCouTypeId());
            records.get(i).setMaxTypeName(courseMapper.getTypeName(typeVO.getParentTypeId()));
            records.get(i).setMinTypeName(typeVO.getTypeName());
            records.get(i).setCouPic(records.get(i).getCouPic());
            records.get(i).setCouCataNum(courseMapper.getCataNumByCouId(records.get(i).getCouId()));
        }
        respPageBean.setTotal(couVOIPage.getTotal());
        respPageBean.setData(records);
        respPageBean.setPages(couVOIPage.getPages());
        return respPageBean;
    }

    @Override
    public void updatePurNum(Long couId, Long purchaseNum) {
        courseMapper.updatePurNum(couId, purchaseNum);
    }

    @Override
    public Long getColletion(Long couId) {
        return courseMapper.getColletion(couId);
    }

    @Override
    public CouAVO courseAppraise(Long couId, IPage<CouAppraiseVO> page) {
        CouAVO couAVO = new CouAVO();
        IPage<CouAppraiseVO> couAppraiseVOIPage = courseMapper.courseAppraise(couId, page);
        Long total = couAppraiseVOIPage.getTotal();
        Double totalScore = total*5.0;
        Long allCouScore = courseMapper.getAllCouScore(couId);
        couAVO.setAllScore(Math.round((float) ((allCouScore*100/totalScore)*5/100.0) * 10) / 10f);
        List<CouAppraiseVO> records = couAppraiseVOIPage.getRecords();
        for (int i=0;i<records.size();i++) {
            StudentDetailedVO stuDetai = remoteUserService.getStuByStuId(records.get(i).getStuId());
            records.get(i).setImage(stuDetai.getImage());
            records.get(i).setStuName(stuDetai.getStuName());
        }
        //评价人数
        couAVO.setNum(couAppraiseVOIPage.getTotal());
        couAVO.setCouAppraiseVOList(records);
        return couAVO;
    }

    @Override
    public CouAppraiseVO getCouAppraiseVO(Long couId, Long stuId) {
       return courseMapper.getCouAppraiseVO(couId, stuId);
    }

    @Override
    public R addCouAppraise(CouAppraiseDTO couAppraiseDTO) {
        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);
        int conclusionType = Sample.sampleText(sampleAip.getAip(), couAppraiseDTO.getCouAppraise());
        if (conclusionType == 1) {
            long comId = new SnowflakeIdWorker(0, 0).nextId();
            if (courseMapper.getCouAppraiseVO(couAppraiseDTO.getCouId(), couAppraiseDTO.getStuId()) == null) {
                couAppraiseDTO.setCouId(comId);
                //新增个人评价
                courseMapper.addCouAppraise(couAppraiseDTO);
                //评价课程加积分(流水)
                courseMapper.addScore(couAppraiseDTO.getStuId(), scoreType, score);
                //评价课程加积分
                int stuScoreByStuId = remoteUserService.getStuScoreByStuId(couAppraiseDTO.getStuId());
                remoteUserService.updateScore(couAppraiseDTO.getStuId(), stuScoreByStuId+score);
            }else {
                courseMapper.updateCouAppraise(couAppraiseDTO);
            }
            return R.ok("评论成功");
        }else {
            return R.error("存在违规内容");
        }
    }

    @Override
    public Float getCouEvSc(Long couId) {
        int total = courseMapper.getTotal(couId);
        Double totalScore = total*5.0;
        Long allCouScore = courseMapper.getAllCouScore(couId);
        return Math.round((float) ((allCouScore*100/totalScore)*5/100.0) * 10) / 10f;
    }

    /**
     * 解析图片名称
     */
//    public String img(String img) {
//        if(img != null && !img.equals("")) {
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath+fileSonPath+"\\"+img);
//                data = new byte[in.available()];
//                in.read(data);
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //进行Base64编码
//            BASE64Encoder encoder = new BASE64Encoder();
//            return "data:image/jpeg;base64,"+encoder.encode(data);
//        }else {
//            return "";
//        }
//    }
}
