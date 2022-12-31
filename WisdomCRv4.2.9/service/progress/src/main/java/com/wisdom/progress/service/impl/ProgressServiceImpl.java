package com.wisdom.progress.service.impl;

import com.wisdom.base.util.*;
import com.wisdom.chapter.bean.vo.ChapterVO;
import com.wisdom.chapter.feign.RemoteChapterService;
import com.wisdom.course.feign.RemoteCourseService;
import com.wisdom.durations.bean.dto.CouCommentDTO;
import com.wisdom.durations.bean.dto.HisToryProgressVO;
import com.wisdom.durations.bean.dto.HistoryLearningDurationUPDTO;
import com.wisdom.durations.bean.dto.ProgressDTO;
import com.wisdom.durations.bean.vo.*;
import com.wisdom.progress.mapper.ProgressMapper;
import com.wisdom.progress.service.ProgressService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import top.banner.lib.idempotent.Idempotent;
import top.banner.lib.lock.RedisLock;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProgressServiceImpl implements ProgressService {
    @Resource
    private ProgressMapper progressMapper;
    @Resource
    private RemoteChapterService remoteChapterService;
    @Resource
    private RemoteCourseService remoteCourseService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${score}")
    public int score;

    @Value("${APP_ID}")
    public String APP_ID;
    @Value("${API_KEY}")
    public String API_KEY;
    @Value("${SECRET_KEY}")
    public String SECRET_KEY;

    @Value("${filesonpath}")
    private String fileSonPath;
    private String filePath;
    private String time; //记录当前日期
    private String yesterday; //记录昨天日期
    private Long endTime = 0L;
    private Long nowTime = 0L;
    Boolean isUP = false;
    final long timeInterval = 1000*60*60*36;
    public ProgressServiceImpl() throws IOException {
        filePath = new File("").getCanonicalPath();// 获取项目根目录
    }

//    @Override
//    public ProgressDTO addClasCouProgress(ProgressDTO progressDTO) {
//        //当期日期yyyy-mm-dd
//        time = DateUtils.getDate();
//        //记录开始学习时间精确到秒
//        endTime = DateUtils.getNowDate().getTime()/1000;
//        if(!stringRedisTemplate.hasKey(time)) {
//            String stuId = progressDTO.getStuId().toString();
//            stringRedisTemplate.opsForValue().set(time+stuId, "0",60*10, TimeUnit.SECONDS);
//            Long aLong = progressMapper.addHisToryProgress(progressDTO.getStuId());
//            stringRedisTemplate.opsForValue().set(stuId, aLong.toString(), 60*10, TimeUnit.SECONDS);
//        }
//        Long startTime  = progressMapper.isRecord(progressDTO);
//        if(startTime == null) {
//            startTime = (long)0;
//        }
//        progressDTO.setStartTime(startTime);
//        progressMapper.addClasCouProgress(progressDTO);
//        return progressDTO;
//    }

//    @RedisLock(name = "addCouProgress", key = "#p0 + ':' + #p1 + ':' + #p2 + ':' + #p3 + ':' + #p4 + ':' + #p5 + ':' + #p6 + ':' + #p7")
    @Idempotent(key = "#p0 + ':' + #p1 + ':' + #p2 + ':' + #p3 + ':' + #p4 + ':' + #p5 + ':' + #p6 + ':' + #p7", expireTime = 3, info = "请勿重复查询")
    @Override
    public void addCouProgress(ProgressDTO progressDTO) {
//        //当期日期yyyy-mm-dd
//        time = DateUtils.getDate();
//        //记录开始学习时间精确到秒
//        endTime = DateUtils.getNowDate().getTime()/1000;
//        if(!stringRedisTemplate.hasKey(time)) {
//
//            stringRedisTemplate.opsForValue().set(time+stuId, "0",60*60*2, TimeUnit.SECONDS);
//            Long aLong = progressMapper.addHisToryProgress(progressDTO.getStuId());
//            stringRedisTemplate.opsForValue().set(stuId, aLong.toString(), 60*60*2, TimeUnit.SECONDS);
//        }
        time = DateUtils.getDate();
        String stuId = progressDTO.getStuId().toString();
        String couId = progressDTO.getCouId().toString();
        //添加学习时长
        if(progressDTO.getIsLeave() == 0 && !stringRedisTemplate.hasKey(stuId+time)) {
            HisToryProgressVO hisToryProgressVO = new HisToryProgressVO();
            hisToryProgressVO.setStuId(progressDTO.getStuId());
            hisToryProgressVO.setCouId(progressDTO.getCouId());
            hisToryProgressVO.setDailyTime(progressDTO.getTime());
            if (!stringRedisTemplate.hasKey(time+stuId)) {
                //记录当前课程时长
                stringRedisTemplate.opsForValue().set(time+stuId, progressDTO.getTime().toString(),60*60*60, TimeUnit.SECONDS);
            }else {
                //更新学习时长
                Long aLong = Long.valueOf(stringRedisTemplate.opsForValue().get(time+stuId));
                //最新时长
                Long newDuration = progressDTO.getTime()+aLong;
                stringRedisTemplate.opsForValue().set(time+stuId, newDuration.toString(), 0);
            }
            stringRedisTemplate.opsForValue().set(stuId+couId, time);
            progressMapper.addHisToryProgress(hisToryProgressVO);
            //记录添加课程时长的id
            stringRedisTemplate.opsForValue().set(stuId+time, hisToryProgressVO.getId().toString(), 60*60*2, TimeUnit.SECONDS);
        }else if (progressDTO.getIsLeave() == 1){
            //从redis获取当前日期（防止过去日期的学习时长没记录）
            String time1 = stringRedisTemplate.opsForValue().get(stuId+couId);
            if (!stringRedisTemplate.hasKey(stuId+time1)) {
                HisToryProgressVO hisToryProgressVO = new HisToryProgressVO();
                hisToryProgressVO.setStuId(progressDTO.getStuId());
                hisToryProgressVO.setCouId(progressDTO.getCouId());
                hisToryProgressVO.setDailyTime(progressDTO.getTime());
                if (!stringRedisTemplate.hasKey(time1+stuId)) {
                    //记录当前课程时长
                    stringRedisTemplate.opsForValue().set(time1+stuId, progressDTO.getTime().toString(),60*60*60, TimeUnit.SECONDS);
                }else {
                    //更新学习时长
                    Long aLong = Long.valueOf(stringRedisTemplate.opsForValue().get(time1+stuId));
                    //最新时长
                    Long newDuration = progressDTO.getTime()+aLong;
                    stringRedisTemplate.opsForValue().set(time1+stuId, newDuration.toString(), 0);
                }
                progressMapper.addHisToryProgress(hisToryProgressVO);
            }else {
                String s = stringRedisTemplate.opsForValue().get(stuId + time1);
                stringRedisTemplate.delete(stuId + time1);
                Long id = Long.valueOf(s);
                //更新学习时长
                Long aLong = Long.valueOf(stringRedisTemplate.opsForValue().get(time1+stuId));
                //最新时长
                Long newDuration = progressDTO.getTime()+aLong;
                stringRedisTemplate.opsForValue().set(time1+stuId, newDuration.toString(), 0);
                Long hisToryProgress = progressMapper.getHisToryProgress(id);
                Long aLong1 = hisToryProgress+progressDTO.getTime();
                progressMapper.updateHisToryProgress(aLong1, id);
                stringRedisTemplate.opsForValue().set(stuId+couId, time);
            }
        }
        if (progressDTO.getClassId() != null && progressDTO.getClassId() != 0L) {
            progressMapper.addClasCouProgress(progressDTO);
        }else {
            progressMapper.addCouProgress(progressDTO);
        }
        //获取学习时长超过多少调用
//        remoteUserService.automaticClock();
    }


    @Override
    public Boolean addHistoryLearningDuration(ProgressDTO progressDTO) {
        //当期日期yyyy-mm-dd
        time = DateUtils.getDate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();//new一个Calendar类,把Date放进去
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        //昨天yyyy-mm-dd
        yesterday = df.format(calendar.getTime());
        //记录开始学习时间精确到秒
        endTime = DateUtils.getNowDate().getTime()/1000;
        if(!stringRedisTemplate.hasKey(time+progressDTO.getStuId())) {
            Long newDuration = null;
            if (stringRedisTemplate.opsForValue().get(yesterday+progressDTO.getStuId()) != null){
                newDuration = Long.valueOf(stringRedisTemplate.opsForValue().get(yesterday+progressDTO.getStuId()));
            }
            String id = stringRedisTemplate.opsForValue().get(progressDTO.getStuId().toString());
            System.out.println(yesterday+" "+newDuration+" "+id+" "+isUP);
            if (newDuration!=null&&id!=null&&isUP==false){
                System.out.println("开始手动修改");
                HistoryLearningDurationUPDTO historyLearningDurationUPDTO = new HistoryLearningDurationUPDTO();
                historyLearningDurationUPDTO.setId(Long.valueOf(id))
                        .setNewDuration(newDuration);
                progressMapper.upHistoryLearningDuration(historyLearningDurationUPDTO);
                isUP = true;
            }else if (isUP==true){
                isUP = false;
            }
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        // sleep()：同步延迟数据，并且会阻塞线程
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("进入线程");
                    if (isUP == false){
                        System.out.println("开始自动修改");
                        Long newDuration = null;
                        if (stringRedisTemplate.opsForValue().get(yesterday+progressDTO.getStuId()) != null){
                            newDuration = Long.valueOf(stringRedisTemplate.opsForValue().get(yesterday+progressDTO.getStuId()));
                        }
//                        if (stringRedisTemplate.opsForValue().get(time+progressDTO.getStuId()) != null){
//                            newDuration = Long.valueOf(stringRedisTemplate.opsForValue().get(time+progressDTO.getStuId()));
//                        }
                        String id = stringRedisTemplate.opsForValue().get(progressDTO.getStuId().toString());
                        if (newDuration!=null&&id!=null){
                            HistoryLearningDurationUPDTO historyLearningDurationUPDTO = new HistoryLearningDurationUPDTO();
                            historyLearningDurationUPDTO.setId(Long.valueOf(id))
                                    .setNewDuration(newDuration);
                            Boolean is = progressMapper.upHistoryLearningDuration(historyLearningDurationUPDTO);
                            if (is == true){
                                isUP = true;
                            }
                        }
                    }
                    //创建定时器
                    Thread newThread = new Thread(this);
                    //开始执行
                    newThread.start();
                }
            };
            //创建定时器
            Thread thread = new Thread(runnable);
            //开始执行
            thread.start();
            stringRedisTemplate.opsForValue().set(time+progressDTO.getStuId(), "0",60*60*36, TimeUnit.SECONDS);
            progressMapper.addHistoryLearningDuration(progressDTO);
            stringRedisTemplate.opsForValue().set(progressDTO.getStuId().toString(), progressDTO.getId().toString(), 60*60*36, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    @Override
    public void upHistoryLearningDuration(ProgressDTO progressDTO) {
        //当前时间精确到秒
        nowTime = DateUtils.getNowDate().getTime()/1000;
        //计算学习时长
        Long learnDuration = nowTime - endTime;
        //更新学习时长
        Long aLong = Long.valueOf(stringRedisTemplate.opsForValue().get(time+progressDTO.getStuId()));
        //最新时长
        Long newDuration = learnDuration+aLong;
        stringRedisTemplate.opsForValue().set(time+progressDTO.getStuId(), newDuration.toString(), 0);
    }

    @Override
    public List<ClassProgressVO> getClasCouProgress(Long stuId) {
        //班级集合
        List<ClassProgressVO> clasCouProgress = progressMapper.getClasCouProgress(stuId);
        for (int i=0;i<clasCouProgress.size();i++) {
            clasCouProgress.get(i).setClassLogo(clasCouProgress.get(i).getClassLogo());
            //课程集合
            List<CourseProgressVO> courseProgressVOS = progressMapper.getClassCou(clasCouProgress.get(i).getClassId());
            for (int j=0;j<courseProgressVOS.size();j++) {
                //课程封面
                courseProgressVOS.get(j).setCouPic(courseProgressVOS.get(j).getCouPic());
                List<ChapterVO> claCourseAllCha = remoteChapterService.getClaCourseAllCha(courseProgressVOS.get(j).getCouId());
                Long allChaTime = 0L;
                for (int a=0;a<claCourseAllCha.size();a++) {
                    allChaTime += claCourseAllCha.get(a).getChaTime();
                }
                //课程中各个章节最大值
                List<Long> classCouAllTime = progressMapper.getClassCouAllTime(clasCouProgress.get(i).getClassId(), courseProgressVOS.get(j).getCouId(), stuId);
                //如果课程还没看，进度为0
                if (classCouAllTime.size() == 0) {
                    courseProgressVOS.get(j).setRatio(0L);
                    continue;
                }
                Long allTime = 0L;
                for (int k=0;k<classCouAllTime.size();k++) {
                    allTime += classCouAllTime.get(k);
                }
                // 每门课程的总学习进度
                courseProgressVOS.get(j).setRatio(allTime*100/allChaTime);
            }
            clasCouProgress.get(i).setCourseProgressVOList(courseProgressVOS);
        }
        return clasCouProgress;
    }

    @Override
    public List<CourseProgressVO> getCouProgress(Long stuId) {
        List<CourseProgressVO> courseProgressVOS = progressMapper.getCouProgress(stuId);
        for (int j=0;j<courseProgressVOS.size();j++) {
            //课程封面
            courseProgressVOS.get(j).setCouPic(courseProgressVOS.get(j).getCouPic());
            List<ChapterVO> claCourseAllCha = remoteChapterService.getClaCourseAllCha(courseProgressVOS.get(j).getCouId());
            Long allChaTime = 0L;
            for (int a=0;a<claCourseAllCha.size();a++) {
                allChaTime += claCourseAllCha.get(a).getChaTime();
            }
            //课程中各个章节最大值
            List<Long> classCouAllTime = progressMapper.getCouAllTime(courseProgressVOS.get(j).getCouId(), stuId);
            Long allTime = 0L;
            for (int k=0;k<classCouAllTime.size();k++) {
                allTime += classCouAllTime.get(k);
            }
            // 每门课程的总学习进度
            if (allChaTime != 0l) {
                courseProgressVOS.get(j).setRatio(allTime*100/allChaTime);
            }else {
                courseProgressVOS.get(j).setRatio(0l);
            }
        }
        return courseProgressVOS;
    }

    @Override
    public CouProgressVO claCourseDetail(Long stuId, Long classId, Long couId) {
        CouProgressVO couProgressVO = new CouProgressVO();
        //查询课程的所有章节
        List<ChapterVO> claCourseAllCha = remoteChapterService.getClaCourseAllCha(couId);
        //章节的学习进度
        List<ChapterProgressVO> chapterProgressVOS = progressMapper.claCourseChaPro(stuId, classId, couId);
        //章节的恢复时长
        List<ChapterProgressVO> chapterProgressVOS1 = progressMapper.clasCourseChaTime(stuId, classId, couId);
        int j=0;
        for (int i=0;i<claCourseAllCha.size();i++) {
            if (j<chapterProgressVOS.size()&&claCourseAllCha.get(i).getChaId().equals(chapterProgressVOS.get(j).getChaId())) { //判断课程的chaId和章节进度中chaId是否相同
                Long totalTime = claCourseAllCha.get(i).getChaTime();
                Long progressTime = chapterProgressVOS.get(j).getProgressTime();
                claCourseAllCha.get(i).setStartTime(chapterProgressVOS1.get(j).getStartTime());
                claCourseAllCha.get(i).setProgressTime(chapterProgressVOS.get(j).getProgressTime());
                claCourseAllCha.get(i).setRatio((int) (progressTime*100/totalTime));
                j++;
            }else {
                claCourseAllCha.get(i).setRatio(0);
                claCourseAllCha.get(i).setProgressTime(0l);
                claCourseAllCha.get(i).setStartTime(0l);
            }
        }
        couProgressVO.setCouIntroduction(remoteCourseService.getCouIntroduction(couId));
        couProgressVO.setChapterVOList(claCourseAllCha);
        couProgressVO.setCompreEvScore(remoteCourseService.getCouEvSc(couId));
        return couProgressVO;
    }

    @Override
    public CouProgressVO courseDetail(Long stuId, Long couId) {
        CouProgressVO couProgressVO = new CouProgressVO();
        //查询课程的所有章节
        List<ChapterVO> claCourseAllCha = remoteChapterService.getClaCourseAllCha(couId);
        //章节的学习进度
        List<ChapterProgressVO> chapterProgressVOS = progressMapper.courseChaPro(stuId, couId);
        //章节的恢复时长
        List<ChapterProgressVO> chapterProgressVOS1 = progressMapper.courseChaTime(stuId, couId);
        int j=0;
        for (int i=0;i<claCourseAllCha.size();i++) {
            if (j<chapterProgressVOS.size()&&claCourseAllCha.get(i).getChaId().equals(chapterProgressVOS.get(j).getChaId())) { //判断课程的chaId和章节进度中chaId是否相同
                Long totalTime = claCourseAllCha.get(i).getChaTime();
                Long progressTime = chapterProgressVOS.get(j).getProgressTime();
                claCourseAllCha.get(i).setStartTime(chapterProgressVOS1.get(j).getStartTime());
                claCourseAllCha.get(i).setProgressTime(chapterProgressVOS.get(j).getProgressTime());
                claCourseAllCha.get(i).setRatio((int) (progressTime*100/totalTime));
                j++;
            }else {
                claCourseAllCha.get(i).setRatio(0);
                claCourseAllCha.get(i).setProgressTime(0l);
                claCourseAllCha.get(i).setStartTime(0l);
            }
        }
        couProgressVO.setCouIntroduction(remoteCourseService.getCouIntroduction(couId));
        couProgressVO.setChapterVOList(claCourseAllCha);
        couProgressVO.setCompreEvScore(remoteCourseService.getCouEvSc(couId));
        return couProgressVO;
    }


    @Override
    public List<CouCommentVO> getCouCommment(Long couId, Long stuId) {
        List<CouCommentVO> couCommment = progressMapper.getCouCommment(couId);
        for (int i=0;i<couCommment.size();i++) {
            couCommment.get(i).setIsClick(progressMapper.isClick(couCommment.get(i).getCouComId(), stuId));
            couCommment.get(i).setIsDrop(progressMapper.isDrop(couCommment.get(i).getCouComId(), stuId));
        }
        return couCommment;
    }

    @Override
    public R addComment(CouCommentDTO couCommentDTO) {
        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);
        //文本内容
        int conclusionType = Sample.sampleText(sampleAip.getAip(), couCommentDTO.getCouComment());
        if (conclusionType == 1) {
            long couComId = new SnowflakeIdWorker(0, 0).nextId();
            couCommentDTO.setCouComId(couComId);
            progressMapper.addComment(couCommentDTO);
            return R.ok("发表成功");
        }else {
            return R.error("发表失败");
        }
    }

    @RedisLock(name = "click", key = "#p0 + ':' + #p1")
    @Override
    public void click(Long couComId, Long stuId) {
        CouCommentVO couCommentVO = progressMapper.getClicks(couComId);
        Long clicks = couCommentVO.getClicks();
        Long drops = couCommentVO.getDrops();
        if (progressMapper.isClick(couComId, stuId) == 1) { //已点赞
            progressMapper.deletedClick(couComId, stuId);
            progressMapper.updateCouComClicks(couComId, clicks-1);
        }else if(progressMapper.isClick(couComId, stuId) == 0){
            if(progressMapper.isDrop(couComId, stuId) == 1) {
                progressMapper.deletedDrop(couComId, stuId);
                progressMapper.updateCouComDrops(couComId, drops-1);
            }
            progressMapper.addClick(couComId, stuId);
            progressMapper.updateCouComClicks(couComId, clicks+1);
        }
    }

    @RedisLock(name = "click", key = "#p0 + ':' + #p1")
    @Override
    public void drop(Long couComId, Long stuId) {
        CouCommentVO couCommentVO = progressMapper.getClicks(couComId);
        Long clicks = couCommentVO.getClicks();
        Long drops = couCommentVO.getDrops();
        if (progressMapper.isDrop(couComId, stuId) == 1) { //已下踩
            progressMapper.deletedDrop(couComId, stuId);
            progressMapper.updateCouComDrops(couComId, drops-1);
        }else if(progressMapper.isDrop(couComId, stuId) == 0){
            if(progressMapper.isClick(couComId, stuId) == 1) {
                progressMapper.deletedClick(couComId, stuId);
                progressMapper.updateCouComClicks(couComId, clicks-1);
            }
            progressMapper.addDrop(couComId, stuId);
            progressMapper.updateCouComDrops(couComId, drops+1);
        }
    }

    @Override
    public String yStudyTime(Long stuId) {
        Long yStudyTime = progressMapper.getYStudyTime(stuId, DateUtils.getDate());
        if (yStudyTime == null) {
            return "0";
        }
        Long l = yStudyTime / 60; //分钟数
        Long s = yStudyTime - l*60; //秒数
//        Long h = l / 60; //小时数
//        Long m = l - h*60; //分钟数
        return l.toString()+"分钟"+s.toString()+"秒";
    }

    @Override
    public String tStudyTime(String stuId) {
        String t = stringRedisTemplate.opsForValue().get(DateUtils.getDate()+stuId);
        if (t == null) {
            return "0秒";
        }
        long tStudyTime = Long.valueOf(t);
        System.out.println(tStudyTime);
        Long l = tStudyTime / 60;//分钟数
        Long s = tStudyTime  - l*60; //秒数
        Long h = l / 60; //小时数
        Long m = l - h*60; //分钟数
        if (h != 0) {
            return h.toString()+"时"+m.toString()+"分"+s.toString()+"秒";
        }else if (m != 0) {
            return l.toString()+"分"+s.toString()+"秒";
        }else {
            return s.toString()+"秒";
        }
    }

    @Override
    public String monthTime(Long stuId) {
        String monthFirstDay = DateUtils.getMonthFirstDay();
        Long monthTime = progressMapper.monthTime(stuId, monthFirstDay);
        if(monthTime == null) {
            return "0秒";
        }
        Long l = monthTime / 60; //分钟数
        Long s = monthTime - l*60; //秒数
        Long h = l / 60; //小时数
        Long m = l - h*60; //分钟数
        if (h != 0) {
            return h.toString()+"时"+m.toString()+"分"+s.toString()+"秒";
        }else if (m != 0) {
            return l.toString()+"分"+s.toString()+"秒";
        }else {
            return s.toString()+"秒";
        }
    }

    @Override
    public ChapterVO chapterProgress(Long stuId, Long chaId, Long classId) {
        ChapterVO chapterVO = new ChapterVO();
        //恢复时间
        if (classId == null ||classId == 0l) {
            chapterVO.setStartTime(progressMapper.getCouStartTime(stuId, chaId));
        }else {
            chapterVO.setStartTime(progressMapper.getClaCouStartTime(stuId, chaId, classId));
        }
        //学习进度
        if (classId == null ||classId == 0l) {
            chapterVO.setProgressTime(progressMapper.getCouProgressTime(chaId, stuId));
        }else {
            chapterVO.setProgressTime(progressMapper.getClasCouProgressTime(chaId, stuId, classId));
        }
        return chapterVO;
    }

    @Override
    public ChapterProgressVO newChapterProgress(Long stuId, Long couId, Long classId) {
        ChapterProgressVO newClaChaStartTime = new ChapterProgressVO();
        if (classId == null ||classId == 0l) {
            newClaChaStartTime = progressMapper.getNewCouChaStartTime(couId, stuId);
            if (newClaChaStartTime == null) {
                ChapterProgressVO cp = new ChapterProgressVO();
                List<ChapterVO> claCourseAllCha = remoteChapterService.getClaCourseAllCha(couId);
                cp.setStartTime(0L);
                cp.setProgressTime(0L);
                cp.setChaId(claCourseAllCha.get(0).getChaId());
                cp.setUrl(claCourseAllCha.get(0).getChaUrl());
                return cp;
            }else {
                //最新的章节和恢复时间
                Long startTime  = progressMapper.isZXRecord(newClaChaStartTime.getChaId(), stuId);
                newClaChaStartTime.setStartTime(startTime);
                newClaChaStartTime.setProgressTime(progressMapper.getCouProgressTime(newClaChaStartTime.getChaId(), stuId));
            }
        }else {
            newClaChaStartTime = progressMapper.getNewClaChaStartTime(couId, classId, stuId);
            if (newClaChaStartTime == null) {
                ChapterProgressVO cp = new ChapterProgressVO();
                List<ChapterVO> claCourseAllCha = remoteChapterService.getClaCourseAllCha(couId);
                cp.setStartTime(0L);
                cp.setProgressTime(0L);
                cp.setChaId(claCourseAllCha.get(0).getChaId());
                cp.setUrl(claCourseAllCha.get(0).getChaUrl());
                return cp;
            }else {
                //最新的章节和恢复时间
                Long startTime  = progressMapper.isRecord(newClaChaStartTime.getChaId(), stuId, classId);
                newClaChaStartTime.setStartTime(startTime);
                newClaChaStartTime.setProgressTime(progressMapper.getClasCouProgressTime(newClaChaStartTime.getChaId(), stuId, classId));
            }
        }
        return newClaChaStartTime;
    }

    @Override
    public void addScore(Long stuId, int scoreType) {
        progressMapper.addScore(stuId, scoreType, score);
    }


    /**
     * 解析图片名称
     */
//    public String img(String img) {
//        if(img != null && !img.equals("")) {
//            InputStream in = null;
//            byte[] data = null;
//            try {
//                in = new FileInputStream(filePath+fileSonPath +"\\"+img);
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
