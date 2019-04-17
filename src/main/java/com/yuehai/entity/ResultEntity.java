package com.yuehai.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhaoyuehai 2019/1/24
 */
@Data
public class ResultEntity implements Serializable {

    private static final long serialVersionUID = -6966628343099521751L;
    /**
     * 状态码
     * <p>
     * 成功：10000
     */
    private String code;
    private String message;
    private String serviceCode;
    private Object data;

}
