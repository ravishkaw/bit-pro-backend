package com.ravi.waterlilly.security;

import com.ravi.waterlilly.model.User;
import com.ravi.waterlilly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// UserDetails service for security
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            throw new UsernameNotFoundException("User with username '" + username + "' does not exist.");
        }

        // Convert roles to granted authorities
        List<GrantedAuthority> grantedAuthorities = existingUser.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                existingUser.getUsername(),
                existingUser.getPassword(),
                Objects.equals(existingUser.getStatus().getName(), "Active"), // Account enabled
                true, // Account non-expired
                true, // Credentials non-expired
                true, // Account non-locked
                grantedAuthorities
        );
    }
}
