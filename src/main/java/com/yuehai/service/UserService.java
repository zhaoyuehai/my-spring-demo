package com.yuehai.service;

import com.yuehai.entity.LoginEntity;
import com.yuehai.entity.UserEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by zhaoyuehai 2019/1/23
 */
public interface UserService {

    /**
     * 登录
     *
     * @param username 用户名/手机号
     * @param password 密码
     */
    LoginEntity login(String username, String password);

    /**
     * 刷新token
     *
     * @param refreshToken refreshToken
     */
    LoginEntity token(String refreshToken);

    /**
     * 注册
     *
     * @param userEntity 用户
     * @return 1：成功 -1：用户名已经注册 -2：手机号已经注册
     */
    int register(UserEntity userEntity);

    UserEntity findUserByName(String username);

    UserEntity findUserByPhone(String phone);

    @PreAuthorize("hasRole('ADMIN') or hasRole('VIP') or hasRole('USER')")
    List<UserEntity> findUsers(int pageNum, int pageSize);

    @PreAuthorize("hasRole('ADMIN') or hasRole('VIP')")
    List<UserEntity> findVips();

    @PreAuthorize("hasRole('ADMIN')")
    int deleteUser(long userId);

    /**
     * 给用户添加角色
     *
     * @param userId   用户ID
     * @param roleCode 角色ID
     * @return int
     */
    @PreAuthorize("hasRole('ADMIN')")
    int addRole(long userId, int roleCode);

    /**
     * 删除用户单个角色
     *
     * @param userId   用户ID
     * @param roleCode 角色ID
     * @return int
     */
    @PreAuthorize("hasRole('ADMIN')")
    int deleteRole(long userId, int roleCode);

    /**
     * 删除用户全部角色
     *
     * @param userId 用户ID
     * @return int
     */
    @PreAuthorize("hasRole('ADMIN')")
    int deleteRoles(long userId);
}
