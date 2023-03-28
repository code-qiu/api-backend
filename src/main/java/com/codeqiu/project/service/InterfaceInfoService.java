package com.codeqiu.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.codeqiu.qapicommon.model.entity.InterfaceInfo;
import com.codeqiu.qapicommon.model.entity.User;
import com.codeqiu.project.model.vo.InterfaceInfoVo;


public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    InterfaceInfoVo getById(long id, User loginUser);
}
