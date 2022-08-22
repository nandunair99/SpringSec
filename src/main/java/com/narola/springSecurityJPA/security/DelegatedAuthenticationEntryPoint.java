package com.narola.springSecurityJPA.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narola.springSecurityJPA.util.ResponseVO;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

//    @Autowired
//    @Qualifier("handlerExceptionResolver")
//    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //resolver.resolveException(request, response, null, authException);
//        ResponseVO<String> responseParams = new ResponseVO<>();
//        responseParams.setErrorCode("Oops,Something went wrong");
//        responseParams.setStatusCode(404);
//        responseParams.setMessage("User not authenticated");
//        responseParams.setData("Something went wrong");
//
//        response.resetBuffer();
//        response.setStatus(404);
//        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//        response.getOutputStream().print(new ObjectMapper().writeValueAsString(responseParams.getData()));
//        response.flushBuffer();
        System.out.println("inside commence method");
    }
}
