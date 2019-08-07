package com.yuehai.controller;

import com.yuehai.entity.ResultEntity;
import com.yuehai.entity.UserEntity;
import com.yuehai.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhaoyuehai 2019/1/23
 */
@RestController
@RequestMapping("api/v1")
public class UserController extends BaseController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "登录")
    @PostMapping(value = "login")
    public ResultEntity login(@RequestParam(value = "username") String username,
                              @RequestParam(value = "password") String password) {
        return userService.login(username, password);
    }

    @ApiOperation(value = "刷新token  ")
    @PostMapping(value = "token")
    public ResultEntity token(@RequestParam(value = "refreshToken") String refreshToken) {
        return userService.token(refreshToken);
    }

    @ApiOperation(value = "注册", notes = "用户密码在服务器存储之前会进行加密")
    @PostMapping(value = "user")
    public ResultEntity register(@RequestBody UserEntity userEntity) {
        return userService.register(userEntity);
    }

    @ApiOperation(value = "更新")
    @PutMapping(value = "user")
    public ResultEntity updateUser(@RequestBody UserEntity userEntity) {
        return userService.updateUser(userEntity);
    }

    @ApiOperation(value = "查询用户", notes = "通过mybatis查MySql数据库")
    @GetMapping(value = "users")
    public ResultEntity users(@ApiParam(name = "pageNum", value = "第几页", required = true) int pageNum, @ApiParam(name = "pageSize", value = "每页数量", required = true) int pageSize) {
        return userService.findUsers(pageNum, pageSize);
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @DeleteMapping(value = "user/{id}")
    public ResultEntity deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    @ApiOperation(value = "查询所有VIP用户", notes = "通过mybatis查MySql数据库")
    @GetMapping(value = "vips")
    public ResultEntity admins() {
        return userService.findVips();
    }
}
