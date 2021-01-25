package cn.xf.distributedsession.controller;

import cn.xf.distributedsession.exception.LoginException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: distributed-session
 * @description:
 * @author: xf
 * @create: 2021-01-24 22:28
 **/
@RestController
@RequestMapping("/user")
public class SessionController {

    private static final String KEY ="remaindertime";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * spring-session
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
     * token+redis实现缓存
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


    /**
     * token+redis实现缓存
     * @param userName
     * @param pwd
     * @return
     */
    @GetMapping("/jwtLogin")
    public String jwtLogin(@RequestParam String userName, @RequestParam String pwd){
        String token =null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
             token = JWT.create()
                    .withClaim("userName",userName)
                     .withClaim("id",1)
                    .withExpiresAt(new Date(System.currentTimeMillis()+360000))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }

        return "登录成功:"+token;
    }

    /**
     *
     * @param
     * @return
     */
    @GetMapping("/jwtInfo")
    public String jwtInfo(@RequestAttribute String userName){
    /*    DecodedJWT jwt =null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
             jwt = verifier.verify(token);
        } catch (JWTVerificationException exception){
            throw new  LoginException();
        }*/
        return "用户名:"+userName;
    }


    @GetMapping("/address")
    public String address(@RequestAttribute Integer uid){

        return "用户信息ID:"+uid;
    }
}
