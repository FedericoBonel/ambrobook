package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.model.UserDetailsImpl;
import com.fedebonel.recipemvc.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username not found")));
    }

}
