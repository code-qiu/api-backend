package com.codeqiu.qapicommon.service;

import com.codeqiu.qapicommon.model.entity.User;

public interface InnerUserService{

    /**
     * 从数据库中查询已经分配的密钥（accessKey，secretKey）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
