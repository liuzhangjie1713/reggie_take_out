package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.common.Result;
import com.liu.entity.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {

    public  Boolean  sendMsg( String phone, HttpSession session);

    public User login(Map map, HttpSession session);

    public void logout(HttpSession session);

}