package com.liu.controller;


import com.liu.common.Result;
import com.liu.entity.User;
import com.liu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;



    // 发送邮箱验证码
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session){
        //  获取邮箱账号
        String phone = user.getPhone();
        if(userService.sendMsg(phone,session)){
            return Result.success("验证码发送成功，请及时查看!");
        }
        return Result.error("验证码发送失败，请重新输入!");

    }

    //用户登录
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session){
        User user = userService.login(map, session );
        if(user != null){
            return Result.success(user);
        }
        return Result.error("登录失败，请重新登录!");
    }

    //用户登出
    @PostMapping("/logout")
    public Result<String> logout(HttpSession session){
        userService.logout(session);
        return Result.success("安全退出成功！");
    }


}
