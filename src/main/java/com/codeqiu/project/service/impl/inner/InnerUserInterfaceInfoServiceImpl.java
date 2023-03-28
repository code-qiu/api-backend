package com.codeqiu.project.service.impl.inner;

import com.codeqiu.project.service.UserInterfaceInfoService;
import com.codeqiu.qapicommon.model.entity.UserInterfaceInfo;
import com.codeqiu.qapicommon.service.InnerUserInterfaceInfoService;
import com.codeqiu.project.common.ErrorCode;
import com.codeqiu.project.exception.BusinessException;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {

        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public boolean getUserLeftNum(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo interfaceInfo = userInterfaceInfoService.query()
                .eq("interfaceInfoId", interfaceInfoId)
                .eq("userId", userId)
                .one();
        return interfaceInfo.getLeftNum() > 0;
    }

    @Override
    public void checkUserInterfaceinfo(long interfaceInfoId, long userId) {
        UserInterfaceInfo interfaceInfo = userInterfaceInfoService.query()
                .eq("interfaceInfoId", interfaceInfoId)
                .eq("userId", userId)
                .one();
        if (interfaceInfo == null) {
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setTotalNum(0);
            userInterfaceInfo.setLeftNum(10);
            userInterfaceInfo.setStatus(0);
            userInterfaceInfoService.save(userInterfaceInfo);
        }
    }
}
