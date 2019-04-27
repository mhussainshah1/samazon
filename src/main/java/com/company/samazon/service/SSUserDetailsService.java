package com.company.samazon.service;

import com.company.samazon.models.User;
import com.company.samazon.models.Role;
import com.company.samazon.models.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public SSUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User myUser = userRepository.findByUsername(username);
            if (myUser == null) {
                return null;
            }
            return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), getAuthorities(myUser));
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Set<GrantedAuthority> getAuthorities(User appuser) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : appuser.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
