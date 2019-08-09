package com.yuehai.entity;

import lombok.Data;

/**
 * Created by zhaoyuehai 2019/4/10
 */
@Data
public class LoginEntity {
    private String accessToken;
    private String refreshToken;
    private String tokenHeader;
    private long expiration;

    private String userName;
    private String nickName;
    private String phone;
    private String email;
    private String avatar;
    private String roleName;
    private int status;

    public LoginEntity(String accessToken, String refreshToken, String tokenHeader, long expiration, UserEntity user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenHeader = tokenHeader;
        this.expiration = expiration;
        this.userName = user.getUserName();
        this.nickName = user.getNickName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.roleName = user.getRoleName();
        this.status = user.getStatus();
    }
}
