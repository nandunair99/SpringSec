package com.narola.springSecurityJPA.exception;


import com.narola.springSecurityJPA.util.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    public ModelAndView validation(UserNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("");
        if (e.getViewName() == null) {
            modelAndView.setViewName("login2");
            modelAndView.addObject("errormsg", e.getMessage());
        } else {
            modelAndView.setViewName(e.getViewName());
            for (Map.Entry<String, String> entry : e.getErrorList().entrySet()) {
                modelAndView.addObject(entry.getKey(), entry.getValue());
            }
        }
        return modelAndView;
    }

    @ExceptionHandler(value = {Exception.class})
    public ModelAndView genericException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error");
        e.getStackTrace();
        modelAndView.addObject("errormsg", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<ResponseVO<String>> handleAuthenticationException(Exception ex) {

        ResponseVO<String> responseParams = new ResponseVO<>();
        responseParams.setErrorCode("Something went wrong");
        responseParams.setStatusCode(404);
        responseParams.setMessage("User not authenticated");
        responseParams.setData("Something went wrong");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseParams);
    }
}