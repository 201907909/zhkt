package com.wisdom.user.service.impl;

import com.wisdom.base.util.R;
import com.wisdom.user.domain.MailReq;
import com.wisdom.user.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.internet.AddressException;
import javax.mail.internet.ParseException;
import java.util.Objects;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    public R sendMail(MailReq mailReq) {
        try {
            if (mailReq.getTo()==null||mailReq.getTo().equals("")) {
                throw new RuntimeException("邮件收信人不能为空");
            }
            if (mailReq.getSubject()==null||mailReq.getSubject().equals("")) {
                throw new RuntimeException("邮件主题不能为空");
            }
            if (mailReq.getText()==null||mailReq.getText().equals("")) {
                throw new RuntimeException("邮件内容不能为空");
            }
            sendMimeMail(mailReq);
            return R.ok("发送邮件成功");
        } catch (Exception e) {
            log.error("发送邮件失败:", e);
            return R.error(e.getMessage());
        }
    }

    private void sendMimeMail(MailReq mailReq) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            mailReq.setFrom(mailSender.getJavaMailProperties().getProperty("from"));
            messageHelper.setFrom(mailSender.getJavaMailProperties().getProperty("from"),mailSender.getJavaMailProperties().getProperty("name"));
            messageHelper.setTo(mailReq.getTo().split(","));
            messageHelper.setSubject(mailReq.getSubject());
            messageHelper.setText(mailReq.getText());
            if (mailReq.getMultipartFiles() != null) {
                for (MultipartFile multipartFile : mailReq.getMultipartFiles()) {
                    messageHelper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), multipartFile);
                }
            }
            mailSender.send(messageHelper.getMimeMessage());
            log.info("发送邮件成功：{}->{}", mailReq.getFrom(), mailReq.getTo());
        }catch (MailPreparationException mse) {
            log.error("发送邮件失败 , MailPreparationException：{}"    , mse.getMessage());
        }catch (MailParseException mse) {
            log.error("发送邮件失败 , MailParseException：{}"   , mse.getMessage());
        }catch (MailAuthenticationException mse) {
            //535 Error: authentication failed 发送人账号，密码，或者授权码 错误
            if (Objects.requireNonNull(mse.getMessage()).contains("authentication failed")){
                log.error("发送邮件失败 ------>>>>>> 发送人邮箱认证失败！ ");
            }else{
                log.error("发送邮件失败 , MailAuthenticationException：{}"   , mse.getMessage());
            }
        }catch (MailSendException mse) {
            // javax.mail.SendFailedException: Invalid Addresses;
            log.error("发送邮件失败 , MailSendException：{}" , mse.getMessage());
            if (Objects.requireNonNull(mse.getMessage()).contains("Invalid Addresses")){
                // 入库
                log.error("发送邮件失败 , MailSendException：{}" ,"目标地址无效");
            }
        }catch (ParseException mse) {
            if(mse instanceof AddressException){
                log.error("发送邮件失败 ,目标地址不合法, AddressException：{}" + mse.getMessage());
            }else{
                log.error("发送邮件失败 , ParseException：" + mse);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
