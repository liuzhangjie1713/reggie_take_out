package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.entity.User;
import com.liu.mapper.UserMapper;
import com.liu.service.UserService;
import com.liu.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${spring.mail.username}")
    private String from;   // 邮件发送人

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 给前端输入的邮箱，发送验证码
     * @param phone
     * @param session
     * @return
     */
    public Boolean sendMsg( String phone, HttpSession session) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("菩提阁验证码邮件");//主题
            //生成随机数
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //  将随机生成的验证码保存到session中
            //session.setAttribute(phone,code);
            // 验证码由保存到session 优化为 缓存到Redis中，并且设置验证码的有效时间为 5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            mailMessage.setText("您收到的验证码是："+code);//内容

            mailMessage.setTo(phone);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 检验验证码是否一致
     * @param map
     * @param session
     * @return
     */
    public User login(Map map, HttpSession session) {

        log.info("userMap:{}" + map.toString());
        // 获取登录表单的 邮箱账号
        String phone = map.get("phone").toString();
        // 获取 验证码
        String code = map.get("code").toString();

        // 从Session中 获取保存的验证码,session 邮箱账号为 key，验证码为value
        //Object codeInSession = session.getAttribute("phone");

        //从Redis中获取缓存验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);

        //  页面提交的验证码 和 Session中保存的验证码 进行比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //  验证比对无误后，可以成功登录
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = getOne(queryWrapper);
            if (user == null) {  // 数据库中没有当前用户，即当前用户为新用户
                //  新用户 自动注册，其信息保存到数据库中
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);  // 设置用户的状态为1，表示用户可以正常使用，为0，则禁用

                save(user);
            }
            // 用户保存到数据库中后，会自动生成userId,
            session.setAttribute("user", user.getId());

            //如果用户登录成功,则删除Redis中缓存的验证码
           redisTemplate.delete(phone);

            //  需要在浏览器端保存用户信息，故返回的数据类型为 Result<User>
            return user;
        }
        return null;
    }

    public void logout(HttpSession session){
        session.removeAttribute("userPhone");
    }
}


