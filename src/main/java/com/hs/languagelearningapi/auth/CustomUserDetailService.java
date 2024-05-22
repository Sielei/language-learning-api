package com.hs.languagelearningapi.auth;

import com.hs.languagelearningapi.common.DTO;
import com.hs.languagelearningapi.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username" +
                        username + " does not exist"));
        return new User(user.email(), user.password(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(DTO.UserDto userDTO) {
        return new ArrayList<>(List.of(new SimpleGrantedAuthority(userDTO.role().toString())));
    }
}
