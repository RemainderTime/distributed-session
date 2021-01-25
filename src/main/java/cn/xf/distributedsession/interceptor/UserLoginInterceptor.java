package cn.xf.distributedsession.interceptor;

import cn.xf.distributedsession.exception.LoginException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: distributed-session
 * @description:
 * @author: xf
 * @create: 2021-01-25 23:33
 **/
@Component
public class UserLoginInterceptor implements HandlerInterceptor{
    private static final String KEY ="remaindertime";
    public static final String USERNAME="userName";
    public static final String UID ="uid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        if(StringUtils.isEmpty(token)){
            throw new RuntimeException("token不能为空");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            request.setAttribute(USERNAME,jwt.getClaim("userName").asString());
            request.setAttribute(UID,jwt.getClaim("id").asInt());

        } catch (JWTVerificationException exception){
            throw new LoginException();
        }catch (JWTCreationException exception){
            throw new RuntimeException("用户未登录");
        }

        return true;
    }
}
