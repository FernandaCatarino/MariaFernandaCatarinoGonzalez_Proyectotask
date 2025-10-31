package com.hitss.springboot.task_manager.Service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitss.springboot.task_manager.Repositories.UserRepository;
import com.hitss.springboot.task_manager.models.User;

@Service
public class JpaUserDetailsService implements UserDetailsService{

    private UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(
                    String.format("Username %s no existe en el sistema!", username));
        }

        User user = userOptional.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName()))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(), 
            true,  // enabled
            true,  // accountNonExpired
            true,  // credentialsNonExpired
            true,  // accountNonLocked
            authorities);
    }

}
