package com.codeqiu.qapicommon.service;

/**
* @author MA_dou
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-03-13 20:40:10
*/
public interface InnerUserInterfaceInfoService {

    boolean invokeCount(long interfaceInfoId, long userId);

    boolean getUserLeftNum(long interfaceInfoId, long userId);

    void checkUserInterfaceinfo(long interfaceInfoId, long userId);
}
