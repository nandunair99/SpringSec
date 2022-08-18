package com.narola.springSecurityJPA.responsehandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narola.springSecurityJPA.util.ResponseVO;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonAuthenticationFailureHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationFailureHandler {

    public JsonAuthenticationFailureHandler(String url) {
        this.setDefaultTargetUrl(url);
    }

    public JsonAuthenticationFailureHandler() {
        this.setDefaultTargetUrl("/error");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("target", this.determineTargetUrl(request, response));

        ResponseVO<Map<String, String>> responseParams = new ResponseVO<>();
        responseParams.setErrorCode("Something went wrong");
        responseParams.setStatusCode(404);
        responseParams.setMessage("User not authenticated");
        responseParams.setData(responseMap);

        logger.info(responseParams.getErrorCode());

        response.resetBuffer();
        response.setStatus(404);
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.getOutputStream().print(new ObjectMapper().writeValueAsString(responseParams.getData()));
        response.flushBuffer(); // marks response as committed -- if we don't do this the request will go through normally!

    }
}
