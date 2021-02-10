package com.bjpowernode.springboot.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.bjpowernode.springboot.blog.web.*.*(..)))")
    public void log(){
        System.out.println("Log running");
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringType() + "."  + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        ReqeustLog reqeustLog = new ReqeustLog(url,ip,classMethod,args);

        logger.info("-------doBefore-------");
        logger.info("Request : {}",reqeustLog);
    }

    @After("log()")
    public void doAfter(){
        logger.info("-------doAfter-------");
    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("-------Return Result------- Result:{}",result);
    }

    private class ReqeustLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public ReqeustLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        public ReqeustLog() {
        }

        @Override
        public String toString() {
            return "ReqeustLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
