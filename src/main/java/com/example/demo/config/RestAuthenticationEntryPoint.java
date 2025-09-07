package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.ZonedDateTime;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        // this will redirect the error "Invalid Token" from auth server to global ControllerAdvice
        
        java.util.Map<String,Object> errors = new java.util.HashMap<>();
        errors.put("message", "Not authenticated");
        errors.put("error", "401");
        errors.put("timestamp", ZonedDateTime.now().toString());
        
        String messageBody = ObjectMapperUtil.getObjectMapper().writeValueAsString(errors);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON);
        httpServletResponse.getWriter().append(messageBody);
        httpServletResponse.setStatus(401);
        resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
    }

}
