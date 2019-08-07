package com.yuehai.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by zhaoyuehai 2019/2/13
 * 参考的文章：
 * https://www.cnblogs.com/hackyo/p/8004928.html
 * http://www.jianshu.com/p/6307c89fe3fa
 * http://www.jianshu.com/p/4468a2fff879
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//启用方法级别的权限认证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private MyUserDetailsService myUserDetailsService;
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private UnauthorizedEntryPoint unauthorizedEntryPoint;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(MyUserDetailsService userDetailsService,
                             JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                             UnauthorizedEntryPoint unauthorizedEntryPoint) {
        this.myUserDetailsService = userDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()//允许跨域
                .csrf().disable() //由于使用的是JWT，我们这里不需要csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//基于token，所以不需要session
                .and()
//                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger-resources/configuration/ui","/swagge‌​r-ui.html").permitAl‌​l()
//                .authorizeRequests()
//                .antMatchers("/",
//                        "/api/v1/hello",
//                        "/*.html",
//                        "/favicon.ico",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js")
//                .permitAll() //允许对于网站静态资源的无授权访问
//                .antMatchers(HttpMethod.POST,
//                        "/api/v1/login",
//                        "/api/v1/token")
//                .permitAll()
//                .anyRequest().authenticated()//其他访问都需要权限认证
//                .and()
                .headers().cacheControl(); // 禁用缓存
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
        //没有权限 --> AccessDeniedException异常已经在BaseController捕获，此处无需处理
        //.accessDeniedHandler(restAccessDeniedHandler);RestAccessDeniedHandler implements AccessDeniedHandler
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
    }
}
