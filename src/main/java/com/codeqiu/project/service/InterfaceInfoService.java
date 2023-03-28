package com.codeqiu.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.codeqiu.qapicommon.model.entity.InterfaceInfo;
import com.codeqiu.qapicommon.model.entity.User;
import com.codeqiu.project.model.vo.InterfaceInfoVo;

/**
* @author Lavender
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-03-15 17:11:14
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    InterfaceInfoVo getById(long id, User loginUser);
}
