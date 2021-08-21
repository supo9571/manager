package com.manager.web.controller.data;

import com.manager.common.core.domain.AjaxResult;
import com.manager.common.core.domain.model.Login;
import com.manager.openFegin.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author marvin 2021/8/21
 */
@RestController
@Api(tags = "玩家登录日志")
@RequestMapping("/data/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    /**
     * 获取登录日志列表
     */
    @PreAuthorize("@ss.hasPermi('data:login:list')")
    @ApiOperation(value = "登录日志列表")
    @PostMapping("/list")
    public AjaxResult list(Login login) {
        return loginService.list(login);
    }

    /**
     * 今日登录总数
     */
    @PreAuthorize("@ss.hasPermi('data:login:list')")
    @ApiOperation(value = "今日登录总数")
    @PostMapping("/today")
    public AjaxResult today() {
        return loginService.selectTodayLogins();
    }

    /**
     * 近期登录数
     */
    @PreAuthorize("@ss.hasPermi('data:login:list')")
    @ApiOperation(value = "近期登录数")
    @PostMapping("/count")
    public AjaxResult count(String type) {
        return loginService.count(type);
    }
}
