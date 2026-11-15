package com.example.ums.security;

import com.example.ums.model.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Loads user details from the database for Spring Security authentication.
 * Queries the base 'users' table so any role (Admin/Teacher/Student) can log in.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JpaRepository<BaseUser, Long> userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser user = ((UserRepository) userRepository).findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}

interface UserRepository extends JpaRepository<BaseUser, Long> {
    java.util.Optional<BaseUser> findByUsername(String username);
}

