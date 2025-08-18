package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.model.user;
import com.stockpulse.stockpulse.repository.userrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private userrepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        user user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new UserDetailImpl(user);
    }

}

