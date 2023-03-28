package com.codeqiu.apiinterface;

import com.codeqiu.apiclientsdk.client.ApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    @Resource
    private ApiClient apiClient;

    @Resource
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("q18115738065@163.com");
        simpleMailMessage.setTo("842782417@qq.com");
        simpleMailMessage.setSubject("title");
        simpleMailMessage.setText("一串小信息");
        javaMailSender.send(simpleMailMessage);
    }


}
