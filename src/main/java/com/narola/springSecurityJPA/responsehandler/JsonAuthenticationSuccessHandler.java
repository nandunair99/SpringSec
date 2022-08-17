package com.narola.springSecurityJPA.responsehandler;

import com.narola.springSecurityJPA.helper.JwtUtil;
import com.narola.springSecurityJPA.helper.ResponseVO;
import com.narola.springSecurityJPA.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("jsonAuthenticationSuccessHandler")
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;
    @Autowired
    JwtUtil jwtUtil;

    private final String forwardUrl;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public JsonAuthenticationSuccessHandler(String url) {
        this.forwardUrl = url;
    }
    public JsonAuthenticationSuccessHandler() {
        this.forwardUrl = "/login";
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        UserDetails userDetails = userService.loadUserByUsername(((User) authentication.getPrincipal()).getUsername());
        String token = this.jwtUtil.generateToken(userDetails);
        Map<String,String> responseMap= new HashMap<>();
        responseMap.put("token",token);
        responseMap.put("target",this.forwardUrl);

        ResponseVO<Map<String,String>> responseParams = new ResponseVO<>();
        responseParams.setErrorCode(null);
        responseParams.setStatusCode(200);
        responseParams.setMessage("User authenticated");
        responseParams.setData(responseMap);

        logger.info(responseParams.getData().get("token"));
        logger.info(responseParams.getData().get("target"));
    }

}
