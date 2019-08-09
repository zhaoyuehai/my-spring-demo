package com.yuehai.service.impl;

import com.github.pagehelper.PageHelper;
import com.yuehai.entity.*;
import com.yuehai.exception.BaseException;
import com.yuehai.mapper.UserMapper;
import com.yuehai.security.JwtTokenUtil;
import com.yuehai.security.MyUsernameNotFoundException;
import com.yuehai.service.UserService;
import com.yuehai.util.ResultUtil;
import com.yuehai.util.StringCheckUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
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
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    public ResultEntity login(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, new String(Base64.decodeBase64(password), StandardCharsets.UTF_8));
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            UserEntity user = findUserByName(username);
            if (user == null) {
                user = findUserByPhone(username);
            }
            if (user != null) {
                // TODO: 2019/4/11 refreshToken
                long expiration = jwtTokenUtil.generateExpiration();
                String token = jwtTokenUtil.generateToken(user.getUserName(), expiration);
                LoginEntity loginEntity = new LoginEntity(token, token, tokenHeader, expiration, user);
                RoleEntity roleEntity = finRoleByUserId(user.getId());
                if (roleEntity != null) {
                    loginEntity.setRoleName(roleEntity.getName());
                }
                return ResultUtil.success(loginEntity, "登录成功");
            } else {
                throw new BaseException(ResultEnum.USER_NOT_FOUND);
            }
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
    public ResultEntity token(String refreshToken) {
        if (!jwtTokenUtil.isTokenExpired(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            UserEntity user = findUserByName(username);
            if (user != null) {
                // TODO: 2019/4/11  refreshToken
                long expiration = jwtTokenUtil.generateExpiration();
                String newToken = jwtTokenUtil.refreshToken(refreshToken, expiration);
                return ResultUtil.success(new LoginEntity(newToken, newToken, tokenHeader, expiration, user));
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
    public ResultEntity register(UserEntity userEntity) {
        if (StringUtils.isEmpty(userEntity.getUserName())) {
            return ResultUtil.error("用户名不能为空");
        }
        if (!StringCheckUtil.isUserName(userEntity.getUserName())) {
            return ResultUtil.error("用户名格式错误【4-16位字母、数字、下划线】");
        }
        if (findUserByName(userEntity.getUserName()) != null) {
            throw new BaseException(ResultEnum.USERNAME_EXIST);
        }
        if (StringUtils.isEmpty(userEntity.getPassword())) {
            return ResultUtil.error("密码不能为空");
        }
        if (!StringCheckUtil.isPassword(userEntity.getPassword())) {
            return ResultUtil.error("密码格式错误【6-16位数字或字母】");
        }
        if (StringUtils.isEmpty(userEntity.getPhone())) {
            return ResultUtil.error("手机号不能为空");
        }
        if (!StringCheckUtil.isChinaPhoneLegal(userEntity.getPhone())) {
            return ResultUtil.error("手机号格式错误");
        }
        if (findUserByPhone(userEntity.getPhone()) != null) {
            throw new BaseException(ResultEnum.PHONE_EXIST);
        }
        String password = passwordEncoder().encode(new String(Base64.decodeBase64(userEntity.getPassword()), StandardCharsets.UTF_8));
        userEntity.setPassword(password);
        userEntity.setStatus(1);//默认激活状态
        int result = userMapper.insertUser(userEntity);
        if (result > 0) {//用户表插入新用户成功后，为该用户设置默认游客角色 --> GUEST
            result = setRole(userEntity.getId(), 2);
        }
        if (result > 0) {
            return ResultUtil.success(userEntity.getId(), "注册成功");
        } else {
            return ResultUtil.error("注册失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultEntity deleteUser(long userId) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = findUserByName(name);
        if (user == null) {
            user = findUserByPhone(name);
        }
        if (user != null) {
            if (user.getId() == userId) throw new BaseException(ResultEnum.DELETE_YOURSELF);//业务：不能删除自己
            RoleEntity roleEntity = userMapper.selectRoleByUserId(userId);
            if (roleEntity.getName().equals("ROLE_ADMIN")) {
                throw new BaseException(ResultEnum.DELETE_ROOT);//业务：不能删除管理员
            }
        } else {
            return ResultUtil.error("用户未登录");
        }
        int result = deleteRole(userId);
        if (result > 0) {
            //删除用户角色后,再删除用户表
            result = userMapper.deleteUser(userId);
        }
        if (result > 0) {
            return ResultUtil.success("删除成功");
        } else {
            return ResultUtil.error("删除失败");
        }
    }

    @Override
    public int setRole(long userId, int roleCode) {
        RoleEntity roleEntity = userMapper.selectRoleByUserId(userId);
        if (roleEntity == null) {//新增
            return userMapper.insertUserRole(userId, roleCode);
        } else {//更新
            return userMapper.updateUserRole(userId, roleCode);
        }
    }

    @Override
    public int deleteRole(long userId) {
        return userMapper.deleteUserRole(userId);
    }

    @Override
    public ResultEntity updateUser(UserEntity userEntity) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = findUserByName(name);
        if (user == null) {
            user = findUserByPhone(name);
        }
        if (user != null) {
            if (user.getId() != userEntity.getId()) {//自己可以修改自己的,管理员可以修改所有人的
                RoleEntity roleEntity = userMapper.selectRoleByUserId(user.getId());
                if (!roleEntity.getName().equals("ROLE_ADMIN")) {
                    throw new BaseException(ResultEnum.MODIFY_OTHERS);//业务：除了管理员外不可以修改别人信息
                }
            }
        } else {
            return ResultUtil.error("用户未登录");
        }
        if (StringUtils.isEmpty(userEntity.getId())) {
            return ResultUtil.error("该用户不存在");
        }
        UserEntity oldUser = findUserById(userEntity.getId());
        if (oldUser == null) {
            return ResultUtil.error("该用户不存在");
        }
        if (StringUtils.isEmpty(userEntity.getPhone())) {
            return ResultUtil.error("手机号不能为空");
        }
        if (!StringCheckUtil.isChinaPhoneLegal(userEntity.getPhone())) {
            return ResultUtil.error("手机号格式错误");
        }
        if (!oldUser.getPhone().equals(userEntity.getPhone())) {//检查手机号是否冲突
            if (findUserByPhone(userEntity.getPhone()) != null) {
                throw new BaseException(ResultEnum.PHONE_EXIST);
            }
        }
        if (StringUtils.isEmpty(userEntity.getEmail())) {//邮箱为非必填项
            userEntity.setEmail(null);
        } else {
            if (!StringCheckUtil.isEmail(userEntity.getEmail())) {
                return ResultUtil.error("邮箱格式错误");
            }
            String oldEmail = oldUser.getEmail();
            if (StringUtils.isEmpty(oldEmail) || !oldEmail.equals(userEntity.getEmail())) {//检查邮箱是否冲突
                if (findUserByEmail(userEntity.getEmail()) != null) {
                    throw new BaseException(ResultEnum.EMAIL_EXIST);
                }
            }
        }
        if (StringUtils.isEmpty(userEntity.getNickName())) {//昵称为非必填项
            userEntity.setNickName(null);
        } else {
            if (!StringCheckUtil.isNickName(userEntity.getNickName())) {
                return ResultUtil.error("昵称格式错误【2-20位中文、英文、数字】");
            }
        }
        int i = userMapper.updateUser(userEntity);
        if (i > 0) {
            return ResultUtil.success("修改成功");
        } else {
            return ResultUtil.error("修改失败");
        }
    }

    @Override
    public ResultEntity findUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserEntity> userEntities = userMapper.selectAllUser();
        ArrayList<UserBean> userBeans = new ArrayList<>();
        for (UserEntity entity : userEntities) {
            UserBean userBean = new UserBean();
            BeanUtils.copyProperties(entity, userBean);
            userBeans.add(userBean);
        }
        return ResultUtil.success(userBeans);
    }

    @Override
    public ResultEntity findVips() {
        return ResultUtil.success(userMapper.selectAllVip());
    }

    @Override
    public UserEntity findUserByName(String username) {
        return userMapper.selectUserByName(username);
    }

    @Override
    public UserEntity findUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    @Override
    public RoleEntity finRoleByUserId(long userId) {
        return userMapper.selectRoleByUserId(userId);
    }

    @Override
    public UserEntity findUserById(long id) {
        return userMapper.selectUserById(id);
    }
}
