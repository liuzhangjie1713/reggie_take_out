package com.liu.interceptor;

import com.alibaba.fastjson.JSON;
import com.liu.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @ClassName:  UserLoginInterceptor
 * @Description: 检查用户是否已经完成登录
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用(Controller方法调用之前)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       try{
           HttpSession session = request.getSession();
           //统一拦截（查询当前session是否存在id）(这里id会在每次登录成功后，写入session)
           Long employeeId = (Long) session.getAttribute("employee");
           Long userId = (Long) session.getAttribute("user");
           if(employeeId != null){
               log.info("用户已登录，用户id为：{}",employeeId);
               return true;
           }
           if(userId != null){
               log.info("用户已登录，用户id为：{}",userId);
               return true;
           }
           response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
       }catch (Exception e){
           e.printStackTrace();
       }
       return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }




}
