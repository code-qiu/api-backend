package com.codeqiu.apiinterface.service;

import com.codeqiu.apiclientsdk.entity.Email;

public interface EmailService {

    String sendEmail(Email email);
}
