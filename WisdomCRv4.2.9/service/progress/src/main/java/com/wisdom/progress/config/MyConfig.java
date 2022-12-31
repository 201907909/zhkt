package com.wisdom.progress.config;//package com.wisdom.progress.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class MyConfig implements WebMvcConfigurer {
//
//    @Bean
//    public MyInter myInterceptor() {
//        return new MyInter();
//    }
//
//    @Autowired
//    public MyInter myInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(myInterceptor)
//                .excludePathPatterns("/user/**","/course/**")
//                .addPathPatterns("/progress/**", "/teacherPractice/test");
//    }
//
//}
