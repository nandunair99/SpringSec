package com.narola.springSecurityJPA.responsehandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String forwardUrl;
    public JsonAuthenticationSuccessHandler(String url)
    {
        this.forwardUrl=url;
    }
    @Override
    public ResponseEntity<String> onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map responseParams=new HashMap<>();
        responseParams.put("status","success");
        responseParams.put("error",null);
        responseParams.put("viewpage",forwardUrl);
        ObjectMapper mapper=new ObjectMapper();

    }
}
