package com.narola.springSecurityJPA.responsehandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narola.springSecurityJPA.security.JwtUtil;
import com.narola.springSecurityJPA.service.UserService;
import com.narola.springSecurityJPA.util.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    private JwtUtil jwtUtil = new JwtUtil();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public JsonAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = userService.loadUserByUsername(((User) authentication.getPrincipal()).getUsername());
        String token = jwtUtil.generateToken(userDetails);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("token", token);
        ResponseVO<Map<String, String>> responseParams = new ResponseVO<>();
        responseParams.setErrorCode(null);
        responseParams.setStatusCode(200);
        responseParams.setMessage("User authenticated");
        responseParams.setData(responseMap);

        logger.info(responseParams.getData().get("token"));
        logger.info(responseParams.getData().get("target"));

        response.resetBuffer();
        response.setStatus(200);
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.getOutputStream().print(new ObjectMapper().writeValueAsString(responseParams.getData()));
        response.flushBuffer(); // marks response as committed -- if we don't do this the request will go through normally!
    }

}
