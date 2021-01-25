package cn.xf.distributedsession.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: distributed-session
 * @description:
 * @author: xf
 * @create: 2021-01-25 22:17
 **/
@ControllerAdvice
public class RuntimeExceptionHandler {


    @ExceptionHandler(LoginException.class)
    @ResponseBody
    public String loginHandler(){

        return "token无效";
    }
}
