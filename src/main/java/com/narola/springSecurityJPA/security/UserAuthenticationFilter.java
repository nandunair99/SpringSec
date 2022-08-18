package com.narola.springSecurityJPA.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narola.springSecurityJPA.responsehandler.JsonAuthenticationSuccessHandler;
import com.narola.springSecurityJPA.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;


public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;
    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();
    private boolean continueChainBeforeSuccessfulAuthentication = false;

    public UserAuthenticationFilter(String defaultFilterProcessesUrl,
                                    AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = null;
        String password = null;
        StringBuffer jb = new StringBuffer();

        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(jb.toString(), Map.class);
            username = map.get("username");
            password = map.get("password");

        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
        return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    public void setAuthenticationSuccessUrl(String url, UserService userService) {
        this.setAuthenticationSuccessHandler(new JsonAuthenticationSuccessHandler(url, userService));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;
        if (!requiresAuthentication(request1, response1)) {
            chain.doFilter(request, response);
            return;
        } else {
            try {
                Authentication authenticationResult = attemptAuthentication(request1, response1);
                if (authenticationResult == null) {
                    return;
                }
                successfulAuthentication(request1, response1, chain, authenticationResult);
            } catch (InternalAuthenticationServiceException failed) {
                this.logger.error("An internal error occurred while trying to authenticate the user.", failed);
                unsuccessfulAuthentication(request1, response1, failed);
            } catch (AuthenticationException ex) {
                // Authentication failed
                unsuccessfulAuthentication(request1, response1, ex);
            }
        }

    }
}
