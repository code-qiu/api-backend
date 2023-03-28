package com.codeqiu.project.model.vo;


import com.codeqiu.qapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子视图
 *
 * @author yupi
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVo extends InterfaceInfo {

    /**
     * 接口调用次数
     */
    private Integer totalNum;

    private Integer leftNum;

    private static final long serialVersionUID = 1L;
}