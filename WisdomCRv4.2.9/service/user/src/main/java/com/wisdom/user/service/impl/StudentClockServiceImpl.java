package com.wisdom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisdom.base.util.DateUtils;
import com.wisdom.base.util.R;
import com.wisdom.stclass.feign.RemoteClassService;
import com.wisdom.user.bean.dto.GetClockDaysOfMonthDTO;
import com.wisdom.user.bean.vo.StuClockDetailsVO;
import com.wisdom.user.bean.vo.StuClockRecordVO;
import com.wisdom.user.domain.ClockDate;
import com.wisdom.user.mapper.StudentClockMapper;
import com.wisdom.user.mapper.StudentMapper;
import com.wisdom.user.service.StudentClockService;
import com.wisdom.user.service.StudentService;
import com.wisdom.user.util.timeDifference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author WisdomCR
 * @since 2022/11/7
 */
@Service
public class StudentClockServiceImpl extends ServiceImpl<StudentClockMapper, ClockDate> implements StudentClockService {
    @Resource
    private StudentClockMapper studentClockMapper;
    @Resource
    private StudentService studentService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RemoteClassService remoteClassService;
    @Resource
    private StudentMapper studentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R clock(Long stuId) {
        try {
            if (stringRedisTemplate.opsForValue().get(stuId+"Clock") != null) {
                return R.error(503,"该用户已打卡");// 该用户已打卡
            }
            Integer score;
            String lastTime = stringRedisTemplate.opsForValue().get(stuId+"LastTime");
            timeDifference t = new timeDifference();
            Long time = t.getNowToNextDaySeconds()+60*60*24;
            //当期日期yyyy-mm-dd
            String nowadays = DateUtils.getDate();
            if (lastTime != null){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();//new一个Calendar类,把Date放进去
                calendar.setTime(date);
                calendar.add(Calendar.DATE, -1);
                //昨天yyyy-mm-dd
                String yesterday = df.format(calendar.getTime());
                String lastScore = stringRedisTemplate.opsForValue().get(stuId+"LastScore");
                if (yesterday.equals(lastTime)){
                    score = Integer.valueOf(lastScore)+1;
                }else {
                    score = 5;
                }
                stringRedisTemplate.delete(stuId+"LastScore");
                stringRedisTemplate.opsForValue().set(stuId+"LastScore", String.valueOf(score), Integer.valueOf(String.valueOf(time)), TimeUnit.SECONDS);
                stringRedisTemplate.delete(stuId+"LastTime");
                stringRedisTemplate.opsForValue().set(stuId+"LastTime", nowadays, Integer.valueOf(String.valueOf(time)), TimeUnit.SECONDS);
            }else {     // 最开始打卡
                score = 5;
                stringRedisTemplate.opsForValue().set(stuId+"LastScore", String.valueOf(score), Integer.valueOf(String.valueOf(time)), TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().set(stuId+"LastTime", nowadays, Integer.valueOf(String.valueOf(time)), TimeUnit.SECONDS);
            }
            ClockDate clockDate = new ClockDate();
            clockDate.setStuId(stuId)
                    .setInfo("+"+score);
            // 插入签到表
            int result = studentClockMapper.insert(clockDate);
            if (result == 1) {
                if (studentService.addScore(stuId, score)) {
                    stringRedisTemplate.opsForValue().set(stuId+"Clock", "1", Integer.valueOf(String.valueOf(time-60*60*24)), TimeUnit.SECONDS);
                    if (stringRedisTemplate.opsForValue().get(stuId+"TomorrowScore") != null){
                        stringRedisTemplate.delete(stuId+"TomorrowScore");
                    }
                    stringRedisTemplate.opsForValue().set(stuId+"TomorrowScore", String.valueOf(score+1), Integer.valueOf(String.valueOf(time)), TimeUnit.SECONDS);
                    GetClockDaysOfMonthDTO getClockDaysOfMonthDTO = new GetClockDaysOfMonthDTO();
                    getClockDaysOfMonthDTO.setStuId(stuId)
                            .setTomorrowScore(score+1);
                    return R.data(getSignInDaysOfTheMonth(getClockDaysOfMonthDTO));    // 返回值改为打卡记录
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // 获取学生某月签到数
    @Override   // 改为查看当前日期所在月份及前4个月份的打开记录
    public StuClockDetailsVO getSignInDaysOfTheMonth(GetClockDaysOfMonthDTO getClockDaysOfMonthDTO) throws ParseException {
        Calendar ca1 = Calendar.getInstance();
        ca1.setTime(new Date());
        Calendar ca2 = Calendar.getInstance();
        ca2.setTime(new Date());
        ca1.add(Calendar.MONTH, -4); //月份减4
        Date lastMonth = ca1.getTime(); //结果
        ca2.add(Calendar.DATE, +1); //日期加1
        Date now1 = ca2.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sf.format(lastMonth).substring(0,7)+"-01";
        String date2 = sf.format(now1);
        List<Map> createTimeList = studentMapper.getStuClockDate(getClockDaysOfMonthDTO.getStuId(), date1, date2);
        List<ClockDate> clockDateList = new ArrayList<>();
        for (Map map:createTimeList){
            String createTime = String.valueOf(map.get("create_time")).substring(0,10);
            String year = createTime.substring(0,4);
            String month = createTime.substring(5,7);
            String day = createTime.substring(8,10);
            String newCreateTime = year+"-"+month+"-"+day;
            ClockDate clockDate = new ClockDate();
            clockDate.setCreateTime(newCreateTime)
                    .setInfo((String) map.get("info"));
            clockDateList.add(clockDate);
        }
        Integer continuedClock = remoteClassService.getStuContinuedClock(getClockDaysOfMonthDTO.getStuId());
        if (getClockDaysOfMonthDTO.getTomorrowScore() == null){
            if (stringRedisTemplate.opsForValue().get(getClockDaysOfMonthDTO.getStuId()+"TomorrowScore") != null){
                getClockDaysOfMonthDTO.setTomorrowScore(Integer.valueOf(stringRedisTemplate.opsForValue().get(String.valueOf(getClockDaysOfMonthDTO.getStuId())+"TomorrowScore")));
            }else {
                getClockDaysOfMonthDTO.setTomorrowScore(5);
            }
        }else {
            if (continuedClock != 0){
                continuedClock+=1;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        List<StuClockRecordVO> stuClockDaysOfMonthVOList = new ArrayList<>();// 保存所有签到的天
        for (int i = 0; i < clockDateList.size(); i++) {
            StuClockRecordVO stuClockRecordVO = new StuClockRecordVO();
            Date now = sdf.parse(String.valueOf(clockDateList.get(i).getCreateTime()));// 将datetime转date
            String time = sdf.format(now);
            stuClockRecordVO.setDate(time)
                    .setInfo(clockDateList.get(i).getInfo());
            stuClockDaysOfMonthVOList.add(stuClockRecordVO);
        }
        StuClockDetailsVO stuClockDetailsVO = new StuClockDetailsVO();
        stuClockDetailsVO.setStuClockRecordVOList(stuClockDaysOfMonthVOList)
                .setContinuedClock(continuedClock)
                .setTomorrowScore(getClockDaysOfMonthDTO.getTomorrowScore());
        return stuClockDetailsVO;
    }

    /**
     * 补签
     *
     * @param getClockDaysOfMonthDTO 学生补签dto对象
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean countersign(GetClockDaysOfMonthDTO getClockDaysOfMonthDTO) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("stu_id", getClockDaysOfMonthDTO.getStuId());
            queryWrapper.like("create_time", getClockDaysOfMonthDTO.getYearDay());
            Long count = studentClockMapper.selectCount(queryWrapper);
            if (count > 0) {
                return false;// 要补签的日期签到过了
            }
            ClockDate clockDate = new ClockDate();
            if (studentService.subScore(getClockDaysOfMonthDTO.getStuId(), 15)) {
                clockDate.setStuId(getClockDaysOfMonthDTO.getStuId())
                        .setCreateTime(getClockDaysOfMonthDTO.getYearDay());
                int result = studentClockMapper.insert(clockDate);
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
}
