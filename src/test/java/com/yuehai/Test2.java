package com.yuehai;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.yuehai.security.JwtTokenUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhaoyuehai 2019/3/29
 */
public class Test2 {
    private static Logger logger = LoggerFactory.getLogger(Test2.class);

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String pwd = "$2a$10$fFD570Fh8UR.beVDRIJykuCOTY6JV5RizJlklIYQQMCxIe8py4W5S";
    @Test
    public void testPWD() {
        boolean matches = passwordEncoder.matches("123456", pwd);
        Assert.assertTrue(matches);
    }

    @Test
    public void testJwt() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("sub", "zhaoyuehai");
        claims.put("created", new Date());
        String token = jwtTokenUtil.generateToken(claims, jwtTokenUtil.generateExpiration());
        logger.info(token);
    }

    @Test
    public void testBase64() {
//        123456 = MTIzNDU2
        String encode = Base64.encode("123456".getBytes());
        logger.info(encode);
    }
}
