package com.narola.springSecurityJPA.service;

import com.narola.springSecurityJPA.exception.UserNotFoundException;
import com.narola.springSecurityJPA.model.Role;
import com.narola.springSecurityJPA.model.User;
import com.narola.springSecurityJPA.repository.RoleRepository;
import com.narola.springSecurityJPA.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleInfo;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user=getUser(username);
       if(user.equals(null))
       {
           throw new UserNotFoundException("User not found");
       }
       return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), user.getRoles());
    }

    public User addUser(User user)
    {
        return userRepository.save(user);
    }
    public Role addRole(Role role)
    {
        return roleRepository.save(role);
    }
    public void addRoleToUser(String username,String roleName)
    {
        User user=userRepository.findByUsername(username);
        Role role=roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }
    public User getUser(String username)
    {
        return userRepository.findByUsername(username);
    }
    public List<User> getAllUser()
    {
        return (List<User>) userRepository.findAll();
    }

}
