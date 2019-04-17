package com.yuehai.controller;

import com.yuehai.entity.ResultEntity;
import com.yuehai.entity.UserEntity;
import com.yuehai.service.UserService;
import com.yuehai.util.ResultUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
        return ResultUtil.success(userService.login(username, password), "登录成功");
    }

    @ApiOperation(value = "刷新token  ")
    @PostMapping(value = "token")
    public ResultEntity token(@RequestParam(value = "refreshToken") String refreshToken) {
        return ResultUtil.success(userService.token(refreshToken));
    }

    @ApiOperation(value = "注册", notes = "用户密码在服务器存储之前会进行加密")
    @PostMapping(value = "register")
    public ResultEntity register(@RequestBody UserEntity userEntity) {
        if (StringUtils.isEmpty(userEntity.getUserName())) {
            return ResultUtil.error("用户名不能为空");
        }
        if (StringUtils.isEmpty(userEntity.getPassword())) {
            return ResultUtil.error("密码不能为空");
        }
        int register = userService.register(userEntity);
        if (register == 1) {
            return ResultUtil.success(1, "注册成功");
        } else {
            return ResultUtil.error("注册失败");
        }
    }

    @ApiOperation(value = "查询所有用户", notes = "通过mybatis查MySql数据库")
    @GetMapping(value = "users")
    public ResultEntity users(@ApiParam(name = "pageNum", value = "第几页", required = true) int pageNum, @ApiParam(name = "pageSize", value = "每页数量", required = true) int pageSize) {
        return ResultUtil.success(userService.findUsers(pageNum, pageSize));
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @DeleteMapping(value = "user/{id}")
    public ResultEntity deleteUser(@PathVariable("id") Long id) {
        int i = userService.deleteUser(id);
        if (i == 1) {
            return ResultUtil.success(1, "删除成功");
        } else {
            return ResultUtil.error("删除失败");
        }
    }

    @ApiOperation(value = "查询所有VIP用户", notes = "通过mybatis查MySql数据库")
    @GetMapping(value = "vips")
    public ResultEntity admins() {
        return ResultUtil.success(userService.findVips());
    }
}
