package com.yuehai.controller;

import com.yuehai.entity.ResultEntity;
import com.yuehai.util.ResultUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaoyuehai 2019/1/23
 */
@RestController
@RequestMapping(value = "api/v1")
public class HelloController extends BaseController {

    @ApiOperation(value = "hello gradle!", notes = "不需要登录")
    @RequestMapping(value = "hello")
    public ResultEntity hello() {
        return ResultUtil.success("hello gradle!");
    }

    @RequestMapping(value = "hello0")
    @PreAuthorize("hasRole('GUEST') or hasRole('VIP') or hasRole('ADMIN')")
    public ResultEntity hello0() {
        return ResultUtil.success("hello gradle! 非游客访问");
    }

    @RequestMapping(value = "hello1")
    @PreAuthorize("hasRole('VIP') or hasRole('ADMIN')")
    public ResultEntity hello1() {
        return ResultUtil.success("hello gradle! 管理员或者VIP访问");
    }

    @RequestMapping(value = "hello2")
    @PreAuthorize("hasRole('GUEST')")
    public ResultEntity hello2() {
        return ResultUtil.success("hello gradle! 游客访问");
    }

}
