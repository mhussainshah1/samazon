package com.company.samazon.service;

import com.company.samazon.models.User;
import com.company.samazon.models.repositories.RoleRepository;
import com.company.samazon.models.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService() {
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveCustomer(User appuser) {
        appuser.setRoles(Arrays.asList(roleRepository.findByRoleName("USER"))
                .stream()
                .collect(Collectors.toSet()));
        userRepository.save(appuser);
    }

    public void saveAdmin(User appuser) {
        appuser.setRoles(Arrays.asList(roleRepository.findByRoleName("ADMIN"))
                .stream()
                .collect(Collectors.toSet()));
        userRepository.save(appuser);
    }

    // returns currently logged in user
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findByUsername(currentUserName);
        return user;
    }

    public String encode(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
    }

    public boolean isUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream()
                .anyMatch(r -> r.getAuthority().equals("USER"));
    }
}


