package com.yuehai.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by zhaoyuehai 2019/4/11
 * 系统在执行到throw new UsernameNotFoundException(...)的时候，
 * 会执行DaoAuthenticationProvider类的retrieveUser方法
 * 在这个方法会捕获UsernameNotFoundException异常:
 * 会执行到父抽象类AbstractUserDetailsAuthenticationProvider的authenticate方法：由于hideUserNotFoundExceptions的值为true，所以这里会new一个新的BadCredentialsException异常抛出来
 */
public class MyUsernameNotFoundException extends AuthenticationException {

    private static final long serialVersionUID = -829115885634226596L;

    public MyUsernameNotFoundException(String msg) {
        super(msg);
    }

    public MyUsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}