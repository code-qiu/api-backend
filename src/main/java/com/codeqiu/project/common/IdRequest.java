package com.codeqiu.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 发布和下线接口请求参数
 *
 * @author yupi
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}