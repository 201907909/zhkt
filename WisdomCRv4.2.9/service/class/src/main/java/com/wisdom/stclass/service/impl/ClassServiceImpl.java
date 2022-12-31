package com.wisdom.stclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.base.util.R;
import com.wisdom.base.util.Sample;
import com.wisdom.base.util.SampleAip;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.course.bean.vo.CourseAboutVO;
import com.wisdom.course.bean.vo.CourseBriefVO;
import com.wisdom.course.feign.RemoteCourseService;
import com.wisdom.stclass.bean.dto.*;
import com.wisdom.stclass.bean.vo.ClassAboutVO;
import com.wisdom.stclass.bean.vo.ClassCourseVO;
import com.wisdom.stclass.bean.vo.ClassDetailedVO;
import com.wisdom.stclass.bean.vo.DailyTimeVO;
import com.wisdom.stclass.domain.Class;
import com.wisdom.stclass.mapper.ClassMapper;
import com.wisdom.stclass.service.ClassService;
import com.wisdom.user.bean.vo.StudentDetailedVO;
import com.wisdom.user.bean.vo.StudentProgressVO;
import com.wisdom.user.feign.RemoteUserService;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WisdomCR
 * @since 2022-10-30
 */
@Service
@Transactional
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Resource
    private ClassMapper classMapper;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private RemoteCourseService remoteCourseService;
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
    public ClassServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }

    @SneakyThrows
    @Override
    public R classFile(MultipartFile multipartFile){
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

    @Override
    public R addClass(ClassAddDTO classAddDTO) {
        Class newClass = new Class();
        newClass.setClassId(classAddDTO.getClassId())
                .setTeaId(classAddDTO.getTeaId())
                .setClassName(classAddDTO.getClassName())
                .setClassLogo(classAddDTO.getClassLogo())
                .setClassDetails(classAddDTO.getClassDetails());
        if (save(newClass)){
            return R.ok("新建班級成功");
        }else {
            return R.error("新建班級失败");
        }
    }

    @Override
    public Boolean deleteClassById(Long id) {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return remove(queryWrapper);
    }

    @SneakyThrows
    @Override
    public R updateClassById(ClassUpdateDTO classUpdateDTO) {
        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);
        int conclusionType = Sample.sampleImage(sampleAip.getAip(), classUpdateDTO.getNewClassLogo());
        if (conclusionType != 1) {
            return R.error("图片存在违规");
        }
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",classUpdateDTO.getId());
        Class newClass = new Class();
        if (classUpdateDTO.getNewClassLogo() != null){
            String filename = classUpdateDTO.getOldClassLogo();
//            File file = new File(filePath + fileSonPath+"\\"+filename);
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
        newClass.setClassName(classUpdateDTO.getClassName())
                .setClassLogo(classUpdateDTO.getNewClassLogo())
                .setClassDetails(classUpdateDTO.getClassDetails());
        if (update(newClass, queryWrapper)){
            return R.ok("修改班級信息成功");
        }else {
            return R.error("修改班級信息失败");
        }
    }

    @Override
    public List<ClassAboutVO> queryClassByTeaId(Long teaId) {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        List<ClassAboutVO> listVo = new ArrayList<>();
        queryWrapper.eq("tea_id", teaId);
        List<Class> list = list(queryWrapper);
        for (Class newClass:list) {
            String classLogo = newClass.getClassLogo();
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath + fileSonPath+"\\"+classLogo);
//                data = new byte[in.available()];
//                in.read(data);
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //进行Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            ClassAboutVO classAboutVO = new ClassAboutVO();
            classAboutVO.setId(newClass.getId())
                    .setClaId(newClass.getClassId())
                    .setClassName(newClass.getClassName())
                    .setClassLogoIO(classLogo)
                    .setClassLogo(classLogo)
                    .setClassDetails(newClass.getClassDetails())
                    .setCreateTime(newClass.getCreateTime());
            listVo.add(classAboutVO);
        }
        return listVo;
    }

    @Override
    public ClassDetailedVO queryClassByClaId(Long id) {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Class newClass = getOne(queryWrapper);
        ClassDetailedVO classDetailedVO = new ClassDetailedVO();
        List<StudentDetailedVO> studentList = remoteUserService.queryStudentByClassId(newClass.getClassId());
        if (studentList.size() != 0){
            for (StudentDetailedVO studentDetailedVO:studentList){
                StudentProgressVO studentProgressVO = remoteUserService.queryStudentProgressByStuId(studentDetailedVO.getStuId());
                if (studentProgressVO != null){
                    studentDetailedVO.setStuDailyTimeStart(studentProgressVO.getStuDailyTimeStart())
                            .setStuDailyTime(studentProgressVO.getStuDailyTime())
                            .setStuClockDate(studentProgressVO.getStuClockDate())
                            .setStuContinuedClock(studentProgressVO.getStuContinuedClock());
                }
                String image = studentDetailedVO.getImage();
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
//            //进行Base64编码
//            BASE64Encoder encoder = new BASE64Encoder();
//            studentDetailedVO.setImageIO("data:image/jpeg;base64,"+encoder.encode(data));
                studentDetailedVO.setImageIO(image);
            }
        }
        List<CourseAboutVO> courseList = remoteCourseService.queryCourseByClassId(newClass.getClassId());
        String classLogo = newClass.getClassLogo();
//        InputStream in = null;
//        byte[] data = null;
//        try {
//            in = new FileInputStream(filePath + fileSonPath+"\\"+classLogo);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //进行Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
        classDetailedVO.setId(id)
                .setClaId(newClass.getClassId())
                .setClassName(newClass.getClassName())
                .setClassLogoIO(classLogo)
                .setClassLogo(classLogo)
                .setClassDetails(newClass.getClassDetails())
                .setClassStuNum(newClass.getClassStuNum())
                .setStudents(studentList)
                .setCourses(courseList);
        return classDetailedVO;
    }

    @Override
    public ClassCourseVO queryClassCourseByClaId(ClassCourseDTO classCourseDTO) {
        List<CourseBriefVO> courseBriefVOList = remoteCourseService.selectCourseByClassId(classCourseDTO.getClassId());
        List<CourseBriefVO> courseBriefVOListAll = remoteCourseService.selectAllCourse(classCourseDTO.getStartIndex(), classCourseDTO.getPageSize(), classCourseDTO.getClassId());
        List<CourseBriefVO> courseBriefVOListAllComplete = remoteCourseService.selectAllCourseComplete();
        String type[] = {"法律法规","康复护理","体育运动","财务会计","现代医学","商业管理","生活常识","编程语言"};
        Map<String, Object> maps[] = new Map[8];
        Map<String, Object> mapAllS[] = new Map[8];
        for (int i=0;i<type.length;i++){
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> mapAll = new HashMap<>();
            map.put("type",type[i]);
            map.put("num",0);
            mapAll.put("type",type[i]);
            mapAll.put("num",0);
            maps[i] = map;
            mapAllS[i] = mapAll;
        }
        if (courseBriefVOList.size() != 0){
            for (CourseBriefVO courseBriefVO:courseBriefVOList){
                switch (courseBriefVO.getParentTypeName()){
                    case "法律法规":
                        Map<String, Object> mapF = maps[0];
                        mapF.put("num", (Integer) mapF.get("num") + 1);
                        maps[0] = mapF;
                        break;
                    case "康复护理":
                        Map<String, Object> mapK = maps[1];
                        mapK.put("num", (Integer) mapK.get("num") + 1);
                        maps[1] = mapK;
                        break;
                    case "体育运动":
                        Map<String, Object> mapT = maps[2];
                        mapT.put("num", (Integer) mapT.get("num") + 1);
                        maps[2] = mapT;
                        break;
                    case "财务会计":
                        Map<String, Object> mapC = maps[3];
                        mapC.put("num", (Integer) mapC.get("num") + 1);
                        maps[3] = mapC;
                        break;
                    case "现代医学":
                        Map<String, Object> mapX = maps[4];
                        mapX.put("num", (Integer) mapX.get("num") + 1);
                        maps[4] = mapX;
                        break;
                    case "商业管理":
                        Map<String, Object> mapS = maps[5];
                        mapS.put("num", (Integer) mapS.get("num") + 1);
                        maps[5] = mapS;
                        break;
                    case "生活常识":
                        Map<String, Object> mapSH = maps[6];
                        mapSH.put("num", (Integer) mapSH.get("num") + 1);
                        maps[6] = mapSH;
                        break;
                    case "编程语言":
                        Map<String, Object> mapB = maps[7];
                        mapB.put("num", (Integer) mapB.get("num") + 1);
                        maps[7] = mapB;
                        break;
                }
            }
        }
        if(courseBriefVOListAllComplete.size()!=0){
            for (CourseBriefVO courseBriefVO:courseBriefVOListAllComplete){
                switch (courseBriefVO.getParentTypeName()){
                    case "法律法规":
                        Map<String, Object> mapF = mapAllS[0];
                        mapF.put("num", (Integer) mapF.get("num") + 1);
                        mapAllS[0] = mapF;
                        break;
                    case "康复护理":
                        Map<String, Object> mapK = mapAllS[1];
                        mapK.put("num", (Integer) mapK.get("num") + 1);
                        mapAllS[1] = mapK;
                        break;
                    case "体育运动":
                        Map<String, Object> mapT = mapAllS[2];
                        mapT.put("num", (Integer) mapT.get("num") + 1);
                        mapAllS[2] = mapT;
                        break;
                    case "财务会计":
                        Map<String, Object> mapC = mapAllS[3];
                        mapC.put("num", (Integer) mapC.get("num") + 1);
                        mapAllS[3] = mapC;
                        break;
                    case "现代医学":
                        Map<String, Object> mapX = mapAllS[4];
                        mapX.put("num", (Integer) mapX.get("num") + 1);
                        mapAllS[4] = mapX;
                        break;
                    case "商业管理":
                        Map<String, Object> mapS = mapAllS[5];
                        mapS.put("num", (Integer) mapS.get("num") + 1);
                        mapAllS[5] = mapS;
                        break;
                    case "生活常识":
                        Map<String, Object> mapSH = mapAllS[6];
                        mapSH.put("num", (Integer) mapSH.get("num") + 1);
                        mapAllS[6] = mapSH;
                        break;
                    case "编程语言":
                        Map<String, Object> mapB = mapAllS[7];
                        mapB.put("num", (Integer) mapB.get("num") + 1);
                        mapAllS[7] = mapB;
                        break;
                }
            }
        }
        ClassCourseVO classCourseVO = new ClassCourseVO();
        classCourseVO.setClassCourseType(maps)
                .setAllCourseType(mapAllS)
                .setCourses(courseBriefVOList)
                .setCourseAll(courseBriefVOListAll)
                .setCourseAllComplete(courseBriefVOListAllComplete);
        return classCourseVO;
    }

    @Override
    public R classAddStuList(ClassAddDelStuSDTO classAddDleStuSDTO) {
        List<Long> stuIds = new ArrayList<>();
        for (Long stuId:classAddDleStuSDTO.getStuIds()){
            ClassAddDelStuDTO classAddDelStuDTO = new ClassAddDelStuDTO();
            classAddDelStuDTO.setClassId(classAddDleStuSDTO.getClassId())
                    .setStuId(stuId);
            Integer isHave = null;
            isHave = classMapper.selectClaStu(classAddDelStuDTO);
            if (isHave == null){
                if (classMapper.classAddStu(classAddDelStuDTO) == null){
                    stuIds.add(stuId);
                }
            }else {
                if (isHave == 1){
                    if (!classMapper.classUpStu(classAddDelStuDTO)){
                        stuIds.add(stuId);
                    }
                }else {
                    stuIds.add(stuId);
                }
            }
        }
        if (stuIds.size()>0){
            List<String> stuNames = remoteUserService.selectStudentName(stuIds);
            String stuNameStr = "";
            for (String stuName:stuNames){
                stuNameStr+=stuName+"、";
            }
            return R.error(stuNameStr.substring(0,stuNameStr.length()-1)+"同学添加失败，可能原因为：已添加该同学或网络延迟");
        }
        return R.ok("班级新增学生成功");
    }

    @Override
    public Boolean classDeletedStuList(ClassAddDelStuSDTO classAddDleStuSDTO) {
        for (Long stuId:classAddDleStuSDTO.getStuIds()){
            ClassAddDelStuDTO classAddDelStuDTO = new ClassAddDelStuDTO();
            classAddDelStuDTO.setClassId(classAddDleStuSDTO.getClassId())
                    .setStuId(stuId);
            Boolean is = classMapper.classDeletedStu(classAddDelStuDTO);
            if (is == false){
                return false;
            }
        }
        return true;
    }

    @Override
    public R classAddCouList(ClassAddDelCouSDTO classAddDelCouSDTO) {
        List<Long> couIds = new ArrayList<>();
        for (Long couId:classAddDelCouSDTO.getCouIds()){
            ClassAddDelCouDTO classAddDelCouDTO = new ClassAddDelCouDTO();
            classAddDelCouDTO.setClassId(classAddDelCouSDTO.getClassId())
                    .setCouId(couId);
            Integer isHave = null;
            isHave = classMapper.selectClaCou(classAddDelCouDTO);
            if (isHave == null){
                if (classMapper.classAddCou(classAddDelCouDTO) == null){
                    couIds.add(couId);
                }
            }else {
                if (isHave == 1){
                    if (!classMapper.classUpCou(classAddDelCouDTO)){
                        couIds.add(couId);
                    }
                }else {
                    couIds.add(couId);
                }
            }
        }
        if (couIds.size()>0){
            List<String> couNames = remoteCourseService.selectCourseName(couIds);
            String couNameStr = "";
            for (String couName:couNames){
                couNameStr+=couName+"、";
            }
            return R.error(couNameStr.substring(0,couNameStr.length()-1)+"课程添加失败，可能原因为：已添加该课程或网络延迟");
        }
        return R.ok("班级新增课程成功");
    }

    @Override
    public Boolean classDeletedCouList(ClassAddDelCouSDTO classAddDelCouSDTO) {
        for (Long couId:classAddDelCouSDTO.getCouIds()){
            ClassAddDelCouDTO classAddDelCouDTO = new ClassAddDelCouDTO();
            classAddDelCouDTO.setClassId(classAddDelCouSDTO.getClassId())
                    .setCouId(couId);
            Boolean is = classMapper.classDeletedCou(classAddDelCouDTO);
            if (is == false){
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<String,Object> getStuDailyTime(Long stuId) {
        List<DailyTimeVO> dailyTimeVOS = classMapper.getStuDailyTime(stuId);
        String stuDailyTimeStart = null;
        List<Integer> defaultList = new ArrayList<>();
        Map<String,Object> defaultMap = new HashMap<>();
        if (dailyTimeVOS.size() == 0){
            List<List<Integer>> listList = new ArrayList<>();
            for (int i=0;i<7;i++){
                defaultList.add(0);
            }
            listList.add(defaultList);
            defaultMap.put("resultTotal",listList);
            defaultMap.put("stuDailyTimeStart",stuDailyTimeStart);
            return defaultMap;
        }else {
            stuDailyTimeStart = String.valueOf(dailyTimeVOS.get(0).getCreateTime());
        }
        List<String> createTimes = new ArrayList<>();
        List<Long> dailyTime = new ArrayList<>();
        for (DailyTimeVO dailyTimeVO:dailyTimeVOS){
            createTimes.add(String.valueOf(dailyTimeVO.getCreateTime()).substring(0,10)+String.valueOf(dailyTimeVO.getCreateTime()).substring(20,28));
            dailyTime.add(dailyTimeVO.getDailyTime());
        }
        List<String> newList = createTimes.stream().distinct().collect(Collectors.toList());    // 去重
        Map<String,Long> dailyTimeM = new HashMap<>();
        for (String create:newList){
            dailyTimeM.put(create,0L);
        }
        for (int i=0;i<createTimes.size();i++){
            String dailyTime1 = createTimes.get(i);
            Long a = dailyTimeM.get(dailyTime1);
            a = a + dailyTime.get(i);
            dailyTimeM.put(dailyTime1,a);
        }
        List<Long> results = new ArrayList<>();
        for (String create:newList){
            Long aa = dailyTimeM.get(create)/60;
            Long a = dailyTimeM.get(create)%60;
            if (a>30){
                aa+=1;
            }
            results.add(aa);
        }
        int index=1;
        Map<String,Object> resultMap = new HashMap<>();
        List<List<Long>> resultTotal = new ArrayList<>();
        for (int i=0;i<newList.size();i++){
            if (newList.get(i).substring(0,7).equals("Mon Nov")){
                List<Long> result = new ArrayList<>();
                resultTotal.add(result);
            }
        }
        if (!newList.get(0).substring(0,7).equals("Mon Nov")){
            List<Long> result = new ArrayList<>();
            resultTotal.add(result);
        }
        List<Long> newResults = new ArrayList<>();
        if (newList.get(0).substring(0,7).equals("Sun Nov")){
            for (int i=0;i<6;i++){
                newResults.add(0L);
            }
            for (int j=0;j<results.size();j++){
                newResults.add(results.get(j));
            }
        }else if (newList.get(0).substring(0,7).equals("Sat Nov")){
            for (int i=0;i<5;i++){
                newResults.add(0L);
            }
            for (int j=0;j<results.size();j++){
                newResults.add(results.get(j));
            }
        }else if (newList.get(0).substring(0,7).equals("Fri Nov")){
            for (int i=0;i<4;i++){
                newResults.add(0L);
            }
            for (int j=0;j<results.size();j++){
                newResults.add(results.get(j));
            }
        }else if (newList.get(0).substring(0,7).equals("Thu Nov")){
            for (int i=0;i<3;i++){
                newResults.add(0L);
            }
            for (int j=0;j<results.size();j++){
                newResults.add(results.get(j));
            }
        }else if (newList.get(0).substring(0,7).equals("Wed Nov")){
            for (int i=0;i<2;i++){
                newResults.add(0L);
            }
            for (int j=0;j<results.size();j++){
                newResults.add(results.get(j));
            }
        }else if (newList.get(0).substring(0,7).equals("Tue Nov")){
            for (int i=0;i<1;i++){
                newResults.add(0L);
            }
            for (int j=0;j<results.size();j++){
                newResults.add(results.get(j));
            }
        }
        for (int j=0,z=0;j<newResults.size();z++,j+=7){
            for (int k=j;k<j+7&&k<newResults.size();k++){
                resultTotal.get(z).add(newResults.get(k));
            }
        }
        if (resultTotal.get(resultTotal.size()-1).size()!=7){
            int size = resultTotal.get(resultTotal.size()-1).size();
            for (int k=0;k<(7-size);k++){
                resultTotal.get(resultTotal.size()-1).add(0L);
            }
        }
        resultMap.put("resultTotal",resultTotal);
        resultMap.put("stuDailyTimeStart",stuDailyTimeStart);
        return resultMap;
    }

    @Override
    public List<String> getStuClockDate(Long stuId) {
        List<Map> createTimeList = classMapper.getStuClockDate(stuId);
        if (createTimeList.size() == 0){
            return null;
        }
        List<String> createTimes = new ArrayList<>();
        for (Map map:createTimeList){
            String createTime = String.valueOf(map.get("create_time")).substring(0,10);
            String year = createTime.substring(0,4);
            String month = createTime.substring(5,7);
            String day = createTime.substring(8,10);
            String newCreateTime = year+"/"+month+"/"+day;
            createTimes.add(newCreateTime);
        }
        return createTimes;
    }

    @Override
    public Integer getStuContinuedClock(Long stuId) {
        List<Map> createTimeList = classMapper.getStuClockDate(stuId);
        if (createTimeList.size() == 0){
            return 0;
        }
        List<String> createTimes = new ArrayList<>();
        for (Map map:createTimeList){
            String createTime = String.valueOf(map.get("create_time")).substring(0,10);
            createTimes.add(createTime);
        }
        Integer count = null;
        try {
            List<Date> signInDates = new ArrayList<>();
            for (String dateStr : createTimes) {
                Calendar calendarTo = Calendar.getInstance();
                calendarTo.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(0);
                c.set(calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH),
                        calendarTo.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                signInDates.add(c.getTime());
            }
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            Long days = null;
            try {
                Date star = dft.parse(dateFormat.format(signInDates.get(signInDates.size()-1)));//开始时间
                Date endDay=dft.parse(dateFormat.format(date));//结束时间
                Long starTime=star.getTime();
                Long endTime=endDay.getTime();
                Long num=endTime-starTime;//时间戳相差的毫秒数
                days = num/24/60/60/1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (days >= 2){
                return 0;
            }
            count = persistentDay(signInDates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return count;
    }

    private static int persistentDay(List<Date> signInDates){
        //定义一个变量表示连续签到天数，从1开始
        int continuousDays = 1;
        //时间已经在数据库中进行了从小到大排序，直接从最大的时间开始往前比较
        Calendar later = Calendar.getInstance();
        Calendar before = Calendar.getInstance();
        for (int i = signInDates.size() - 1; i > 0; i--){
            later.setTime(signInDates.get(i));
            before.setTime(signInDates.get(i - 1));
            //前一天 + 1天 = 后一天，则视为连续签到
            before.add(Calendar.DAY_OF_MONTH,1);
            if (later.get(Calendar.YEAR) == before.get(Calendar.YEAR)
                    && later.get(Calendar.MONTH) == before.get(Calendar.MONTH)
                    && later.get(Calendar.DAY_OF_YEAR) == before.get(Calendar.DAY_OF_YEAR)){
                continuousDays++;
            }else {
                //只要遇到不连续的就不用再往前比较了
                break;
            }
        }
        return continuousDays;
    }

}
