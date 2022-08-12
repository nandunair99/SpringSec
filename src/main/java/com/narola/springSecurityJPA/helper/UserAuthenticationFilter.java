package com.narola.springSecurityJPA.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narola.springSecurityJPA.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;


public class UserAuthenticationFilter extends OncePerRequestFilter {


    private static AuthenticationManager authenticationManager;

    public UserAuthenticationFilter(AuthenticationManager authenticationManager)
    {
        if(authenticationManager!=null)
        this.authenticationManager=authenticationManager;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        StringBuffer jb = new StringBuffer();
        if ((username == null) || (password == null)) {

            String line = null;
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(jb.toString(), Map.class);
                username = map.get("username");
                password = map.get("password");

            } catch (Exception e) { /*report an error*/ }
        }

        if(this.authenticationManager!=null)
        {
            try {
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            } catch (UsernameNotFoundException e) {
                throw new UserNotFoundException(e.getMessage());
            }

        }
        filterChain.doFilter(request, response);
    }
}
