package com.yuehai.service;

import com.yuehai.entity.ResultEntity;
import com.yuehai.entity.UserEntity;
import org.springframework.security.access.prepost.PreAuthorize;

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
    ResultEntity login(String username, String password);

    /**
     * 刷新token
     *
     * @param refreshToken refreshToken
     */
    ResultEntity token(String refreshToken);

    /**
     * 注册
     *
     * @param userEntity 用户
     */
    ResultEntity register(UserEntity userEntity);

    UserEntity findUserById(long id);

    UserEntity findUserByName(String username);

    UserEntity findUserByPhone(String phone);

    UserEntity findUserByEmail(String phone);

    //@PreAuthorize("hasRole('ADMIN') or hasRole('VIP') or hasRole('USER')")
    ResultEntity findUsers(int pageNum, int pageSize);

//    @PreAuthorize("hasRole('ADMIN') or hasRole('VIP')")
    ResultEntity findVips();

//    @PreAuthorize("hasRole('ADMIN')")
    ResultEntity deleteUser(long userId);

    ResultEntity updateUser(UserEntity userEntity);

    /**
     * 给用户添加角色
     *
     * @param userId   用户ID
     * @param roleCode 角色ID
     * @return int
     */
//    @PreAuthorize("hasRole('ADMIN')")
    int addRole(long userId, int roleCode);

    /**
     * 删除用户单个角色
     *
     * @param userId   用户ID
     * @param roleCode 角色ID
     * @return int
     */
//    @PreAuthorize("hasRole('ADMIN')")
    int deleteRole(long userId, int roleCode);

    /**
     * 删除用户全部角色
     *
     * @param userId 用户ID
     * @return int
     */
//    @PreAuthorize("hasRole('ADMIN')")
    int deleteRoles(long userId);
}
