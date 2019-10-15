package com.liseh.auth.service.impl;

import com.liseh.auth.exception.BaseException;
import com.liseh.auth.model.LisehUserDetails;
import com.liseh.auth.persistence.entity.User;
import com.liseh.auth.repository.UserRepository;
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
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BaseException("No user exists by username " + username);
        }
        return new LisehUserDetails(user);
    }
}
