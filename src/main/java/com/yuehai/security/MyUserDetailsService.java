package com.yuehai.security;

import com.yuehai.entity.RoleEntity;
import com.yuehai.entity.UserEntity;
import com.yuehai.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyuehai 2019/2/13
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserMapper userMapper;

    @Autowired
    public MyUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 授权的时候是对角色授权，认证的时候应该基于资源
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        UserEntity user = userMapper.selectUserByName(username);
        if (user == null)//用户名查找
            user = userMapper.selectUserByPhone(username);
        if (user == null)//手机号查找
            throw new MyUsernameNotFoundException(String.format("No user found with '%s'.", username));
        if (user.getStatus() == 2) {
            throw new LockedException("User unavailable.");
        } else if (user.getStatus() == 3) {
            throw new DisabledException("User disabled.");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        RoleEntity role = userMapper.selectRoleByUserId(user.getId());
        if (role != null) {
//        roles.forEach(roleEntity -> authorities.add(new SimpleGrantedAuthority(roleEntity.getName())));
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new User(user.getUserName(), user.getPassword(), authorities);
    }
}
