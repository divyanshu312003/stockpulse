package com.stockpulse.stockpulse.config;

import com.stockpulse.stockpulse.model.user;
import com.stockpulse.stockpulse.repository.userrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserUtil {

    @Autowired
    private userrepository userRepository;

    public user getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
        throw new RuntimeException("Unauthorized");
    }
}
