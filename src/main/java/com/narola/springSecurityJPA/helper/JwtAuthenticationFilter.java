package com.narola.springSecurityJPA.helper;

import com.narola.springSecurityJPA.exception.UserNotFoundException;
import com.narola.springSecurityJPA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    UserService userService;


    @Autowired
    JwtUtil jwtUtil;

    String username=null;
    String jwtToken=null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader=request.getHeader("Authorization");

        if((requestTokenHeader!=null)&&(requestTokenHeader.startsWith("Bearer ")))
        {
            try {
                jwtToken = requestTokenHeader.substring(7);
                username = this.jwtUtil.extractUsername(jwtToken);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if((username!=null)&& (SecurityContextHolder.getContext().getAuthentication()==null))
            {
                UserDetails userDetails=this.userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
//            else
//            {
//                throw new UserNotFoundException("User not found");
//            }

        }
//        else
//        {
//            throw new UserNotFoundException("User not found");
//        }
        filterChain.doFilter(request,response);

    }
}
