package com.codeqiu.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeqiu.project.exception.BusinessException;
import com.codeqiu.project.mapper.InterfaceInfoMapper;
import com.codeqiu.project.model.vo.InterfaceInfoVo;
import com.codeqiu.project.service.InterfaceInfoService;
import com.codeqiu.project.service.UserInterfaceInfoService;
import com.codeqiu.qapicommon.model.entity.InterfaceInfo;
import com.codeqiu.qapicommon.model.entity.User;
import com.codeqiu.qapicommon.model.entity.UserInterfaceInfo;
import com.codeqiu.project.common.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public InterfaceInfoVo getById(long id, User loginUser) {
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"接口不存在");
        }
        InterfaceInfoVo interfaceInfoVO = new InterfaceInfoVo();
        BeanUtils.copyProperties(interfaceInfo,interfaceInfoVO);
        //查询该用户剩余调用接口次数
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId",id);
        queryWrapper.eq("userId",loginUser.getId());
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        if (userInterfaceInfo != null){
            interfaceInfoVO.setLeftNum(userInterfaceInfo.getLeftNum());
            interfaceInfoVO.setTotalNum(userInterfaceInfo.getTotalNum());
        }
        return interfaceInfoVO;
    }
}




