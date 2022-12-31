package com.wisdom.user.controller;

import com.wisdom.base.util.R;
import com.wisdom.user.domain.MailReq;
import com.wisdom.user.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@RestController
@RequestMapping(value = "mail")
@CrossOrigin
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("registerCode")
    public R registerCode(String to){
        String a=(int)(Math.random()*10)+"";
        String b=(int)(Math.random()*10)+"";
        String c=(int)(Math.random()*10)+"";
        String d=(int)(Math.random()*10)+"";
        String verification=a+b+c+d;
        MailReq mailReq = new MailReq();
        mailReq.setTo(to);
        mailReq.setSubject("用户注册验证码");
        mailReq.setText("亲爱的用户您好,您的验证码是"+verification+"");
        return mailService.sendMail(mailReq).put("verification",verification);
    }

    @PostMapping("forgetPassword")
    public R forgetPassword(String to){
        String a=(int)(Math.random()*10)+"";
        String b=(int)(Math.random()*10)+"";
        String c=(int)(Math.random()*10)+"";
        String d=(int)(Math.random()*10)+"";
        String e=(int)(Math.random()*10)+"";
        String f=(int)(Math.random()*10)+"";
        String verification=a+b+c+d+e+f;
        MailReq mailReq = new MailReq();
        mailReq.setTo(to);
        mailReq.setSubject("忘记密码");
        mailReq.setText("亲爱的用户您好,您的验证码是"+verification+"");
        return mailService.sendMail(mailReq).put("verification",verification);
    }

    @PostMapping("updateOrder")
    public R updateOrder(String to,String orderId){
        MailReq mailReq = new MailReq();
        mailReq.setTo(to)
                .setSubject("改票成功")
                .setText("亲爱的用户您好,你的改票成功"+orderId);
        return mailService.sendMail(mailReq);
    }

    @PostMapping("addOrder")
    public R addOrder(String to,String orderId){
        MailReq mailReq = new MailReq();
        mailReq.setTo(to);
        mailReq.setSubject("预定成功");
        mailReq.setText("亲爱的用户您好,你的票预定成功"+orderId);
        return mailService.sendMail(mailReq);
    }

}
