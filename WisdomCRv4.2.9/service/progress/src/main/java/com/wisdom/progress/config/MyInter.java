package com.wisdom.progress.config;//package com.wisdom.progress.config;
//
//import com.wisdom.base.util.DateUtils;
//import com.wisdom.progress.mapper.ProgressMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//@Component
//public class MyInter implements HandlerInterceptor {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private ProgressMapper progressMapper;
//
//    /**
//     * @return 返回 true 放行、放回 false 拦截
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("进入拦截器，前置通知" + request.getRequestURI());
//
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        //获取学生Id
////        String stuId = request.getHeader("stuId");
////
////        Calendar cal = Calendar.getInstance();
////        cal.add(Calendar.DATE,   -1);
////        String yesterday = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
////        String nowday = DateUtils.getDate();
////        //只要访问学习进度接口都会取判断
////        //不存在就说明要更新昨天的历史学习时长到数据库
////        if(!stringRedisTemplate.hasKey(nowday)) {
////            String s = stringRedisTemplate.opsForValue().get(yesterday);
////            String s1 = stringRedisTemplate.opsForValue().get(stuId);
////            progressMapper.updateHisToryProgress(Long.valueOf(s), Long.valueOf(s1));
////        }
//    }
//}
