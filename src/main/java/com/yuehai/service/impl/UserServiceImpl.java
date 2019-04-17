package com.yuehai.service.impl;

import com.github.pagehelper.PageHelper;
import com.yuehai.entity.LoginEntity;
import com.yuehai.entity.ResultEnum;
import com.yuehai.entity.RoleEntity;
import com.yuehai.entity.UserEntity;
import com.yuehai.exception.BaseException;
import com.yuehai.mapper.UserMapper;
import com.yuehai.security.JwtTokenUtil;
import com.yuehai.security.MyUsernameNotFoundException;
import com.yuehai.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by zhaoyuehai 2019/1/23
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    private UserMapper userMapper;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public LoginEntity login(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, new String(Base64.decodeBase64(password), StandardCharsets.UTF_8));
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            UserEntity user = findUserByName(username);
            if (user == null) {
                user = findUserByPhone(username);
            }
            // TODO: 2019/4/11 refreshToken
            long expiration = jwtTokenUtil.generateExpiration();
            String token = jwtTokenUtil.generateToken(user.getUserName(), expiration);
            return new LoginEntity(token, token, tokenHeader, expiration, user);
        } catch (Exception e) {
            if (e.getCause() instanceof LockedException) {
                throw new BaseException(ResultEnum.USER_LOCKED);
            } else if (e.getCause() instanceof DisabledException) {
                throw new BaseException(ResultEnum.USER_DISABLED);
            } else if (e instanceof BadCredentialsException) {
                throw new BaseException(ResultEnum.PASSWORD_ERROR);
            } else if (e.getCause() instanceof MyUsernameNotFoundException) {
                throw new BaseException(ResultEnum.USER_NOT_FOUND);
            } else {
                throw new BaseException(ResultEnum.ERROR);
            }
        }
    }

    @Override
    public LoginEntity token(String refreshToken) {
        if (!jwtTokenUtil.isTokenExpired(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            UserEntity user = findUserByName(username);
            if (user != null) {
                // TODO: 2019/4/11  refreshToken
                long expiration = jwtTokenUtil.generateExpiration();
                String newToken = jwtTokenUtil.refreshToken(refreshToken, expiration);
                return new LoginEntity(newToken, newToken, tokenHeader, expiration, user);
            } else {
                throw new BaseException(ResultEnum.LOGIN_INFO_ERROR);
            }
        } else {
            throw new BaseException(ResultEnum.LOGIN_INFO_EXPIRED);
        }
    }

    //在插入到tb_user和tb_user_role两张表的过程中，出现错误就回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int register(UserEntity userEntity) {
        if (findUserByName(userEntity.getUserName()) != null) {
            throw new BaseException(ResultEnum.USERNAME_EXIST);
        }
        if (findUserByPhone(userEntity.getPhone()) != null) {
            throw new BaseException(ResultEnum.PHONE_EXIST);
        }
        String password = passwordEncoder().encode(new String(Base64.decodeBase64(userEntity.getPassword()), StandardCharsets.UTF_8));
        userEntity.setPassword(password);
        userEntity.setStatus(1);//默认激活状态
        int i = userMapper.insertUser(userEntity);
        if (i == 1) {//用户表插入新用户成功后，为该用户设置默认游客角色 --> GUEST
            UserEntity user = findUserByName(userEntity.getUserName());
            if (user != null) {
                i = addRole(user.getId(), 2);
            }
        }
        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteUser(long userId) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = findUserByName(name);
        if (user == null) {
            user = findUserByPhone(name);
        }
        if (user.getId() == userId) throw new BaseException(ResultEnum.DELETE_YOURSELF);//业务：不能删除自己
        List<RoleEntity> roleEntities = userMapper.selectRoleByUserId(userId);
        for (RoleEntity roleEntity : roleEntities) {
            if (roleEntity.getName().equals("ROLE_ADMIN")) {
                throw new BaseException(ResultEnum.DELETE_ROOT);//业务：不能删除管理员
            }
        }
        int i = userMapper.deleteUser(userId);
        if (i == 1) {
            //用户表删除用户成功后，删除用户角色
            i = deleteRoles(userId);
        }
        return i;
    }

    @Override
    public int addRole(long userId, int roleCode) {
//        不重复插入
        List<RoleEntity> roleEntities = userMapper.selectRoleByUserId(userId);
        for (RoleEntity roleEntity : roleEntities) {
            if (roleEntity.getCode() == roleCode) {
                return 1;
            }
        }
        return userMapper.insertUserRole(userId, roleCode);
    }

    @Override
    public int deleteRole(long userId, int roleCode) {
        return userMapper.deleteUserRole(userId, roleCode);
    }

    @Override
    public int deleteRoles(long userId) {
        return userMapper.deleteUserRoles(userId);
    }

    @Override
    public List<UserEntity> findUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.selectAllUser();
    }

    @Override
    public List<UserEntity> findVips() {
        return userMapper.selectAllVip();
    }

    @Override
    public UserEntity findUserByName(String username) {
        return userMapper.selectUserByName(username);
    }

    @Override
    public UserEntity findUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }
}
