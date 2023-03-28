package com.codeqiu.apiinterface.controller;

import com.codeqiu.apiinterface.service.EmailService;
import com.codeqiu.apiclientsdk.entity.Email;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Resource
    private EmailService emailService;

    @PostMapping
    public String getEmail(@RequestBody Email email) {
        return emailService.sendEmail(email);
    }
}
