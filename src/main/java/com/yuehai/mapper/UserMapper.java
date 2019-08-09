package com.yuehai.mapper;

import com.yuehai.entity.RoleEntity;
import com.yuehai.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyuehai 2019/1/23
 */
public interface UserMapper {

    int insertUser(UserEntity userEntity);

    int deleteUser(long id);

    int updateUser(UserEntity userEntity);

    UserEntity selectUserById(long id);

    UserEntity selectUserByName(String username);

    UserEntity selectUserByPhone(String phone);

    UserEntity selectUserByEmail(String email);

    List<UserEntity> selectAllUser();

    List<UserEntity> selectAllVip();

    /**
     * 插入用户角色
     */
    int insertUserRole(@Param("userId") long userId, @Param("roleCode") int roleCode);

    /**
     * 更新用户角色
     */
    int updateUserRole(@Param("userId") long userId, @Param("roleCode") int roleCode);

    /**
     * 删除角色
     *
     * @param userId 用户ID
     */
    int deleteUserRole(@Param("userId") long userId);

    RoleEntity selectRoleByUserId(long id);

}
