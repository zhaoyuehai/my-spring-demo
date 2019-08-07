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

    int insertUserRole(@Param("userId") long userId, @Param("roleCode") int roleCode);

    /**
     * 删除用户全部角色
     *
     * @param userId 用户ID
     */
    int deleteUserRoles(long userId);

    /**
     * 删除单个角色
     *
     * @param userId   用户ID
     * @param roleCode 角色代码
     */
    int deleteUserRole(@Param("userId") long userId, @Param("roleCode") int roleCode);

    List<RoleEntity> selectRoleByUserId(long id);
}
