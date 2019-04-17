package com.yuehai.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhaoyuehai 2019/1/23
 */
@Data
public class UserEntity {
    private long id;
    private String userName;
    private String password;
    private String email;
    private String nickName;
    private String phone;
    private String avatar;
    private int status;
    private Date createTime;
    private Date updateTime;
}
