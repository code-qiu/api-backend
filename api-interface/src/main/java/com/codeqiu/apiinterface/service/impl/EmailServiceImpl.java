package com.codeqiu.apiinterface.service.impl;

import com.codeqiu.apiinterface.service.EmailService;
import com.codeqiu.apiclientsdk.entity.Email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public String sendEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(email.getSubject());
        message.setText(email.getText());
        message.setTo(email.getTo());
        message.setFrom("q18115738065@163.com");
        javaMailSender.send(message);

        return "发送成功,请查看邮箱";
    }
}
