package com.codeqiu.qapicommon.service;

import com.codeqiu.qapicommon.model.entity.InterfaceInfo;


public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询接口是否存在(请求路径，请求方法)
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
