package com.codeqiu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeqiu.project.common.ErrorCode;
import com.codeqiu.project.exception.BusinessException;
import com.codeqiu.project.mapper.UserInterfaceInfoMapper;
import com.codeqiu.project.service.UserInterfaceInfoService;
import com.codeqiu.qapicommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

/**
* @author Lavender
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-03-19 15:11:41
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 创建时，用户和接口必须存在
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口或用户不存在");
            }
        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "剩余次数不能小于 0");
        }
    }

    /**
     * 用户调用方法，将剩余调用次数 -1 总调用次数 +1
     * @param interfaceInfoId 被调用接口信息 id
     * @param userId 调用的接口的用户 id
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 进行校验
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

            UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("interfaceInfoId", interfaceInfoId);
            updateWrapper.eq("userId", userId);
            updateWrapper.gt("leftNum", 0);
            updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
            return this.update(updateWrapper);
    }
}




