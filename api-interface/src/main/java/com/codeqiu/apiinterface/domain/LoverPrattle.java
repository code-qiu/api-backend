package com.codeqiu.apiinterface.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 土味情话表
 * @TableName lover_prattle
 */
@TableName(value ="lover_prattle")
@Data
public class LoverPrattle implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 情话
     */
    private String loverName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}