package com.narola.springSecurityJPA.Controller;

import com.narola.springSecurityJPA.helper.JwtUtil;
import com.narola.springSecurityJPA.model.JwtResponseDto;
import com.narola.springSecurityJPA.model.LoginDto;
import com.narola.springSecurityJPA.model.User;
import com.narola.springSecurityJPA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @PostMapping(value = "/login2")
    public ResponseEntity<JwtResponseDto> generateToken(@RequestBody LoginDto loginDto) {
//        try
//        {
//            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
//        }
//        catch(UsernameNotFoundException e)
//        {
//            throw new UserNotFoundException(e.getMessage());
//        }

        UserDetails userDetails = this.userService.loadUserByUsername(loginDto.getUsername());
        String token = this.jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }
}
