package com.narola.springSecurityJPA.Controller;

import com.narola.springSecurityJPA.model.LoginDto;
import com.narola.springSecurityJPA.model.User;
import com.narola.springSecurityJPA.security.JwtUtil;
import com.narola.springSecurityJPA.service.UserService;
import com.narola.springSecurityJPA.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(Constants.GETUSERS)
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @PostMapping(value = Constants.LOGIN)
    public void generateToken(@RequestBody LoginDto loginDto) {
        System.out.println("Inside login2 api");
    }
}
