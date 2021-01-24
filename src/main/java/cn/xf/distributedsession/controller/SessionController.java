package cn.xf.distributedsession.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: distributed-session
 * @description:
 * @author: xf
 * @create: 2021-01-24 22:28
 **/
@RestController
public class SessionController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 传统session
     * @param userName
     * @param pwd
     * @param session
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam String userName, @RequestParam String pwd, HttpSession session){
        session.setAttribute("userName",userName);

        return "登录成功";
    }

    /**
     *
     * @param session
     * @return
     */
    @GetMapping("/info")
    public String info(HttpSession session){

        return "用户名:"+session.getAttribute("userName");
    }

    /**
     * session+token实现缓存
     * @param userName
     * @param pwd
     * @return
     */
    @GetMapping("/tokenLogin")
    public String tokenLogin(@RequestParam String userName, @RequestParam String pwd){
        String token ="Token:"+ UUID.randomUUID();

        stringRedisTemplate.opsForValue().set(token,userName,3600, TimeUnit.SECONDS);

        return "登录成功:"+token;
    }

    @GetMapping("/tokenInfo")
    public String tokenInfo(@RequestHeader String token){

        String s = stringRedisTemplate.opsForValue().get(token);

        return "用户名:"+s;
    }
}
