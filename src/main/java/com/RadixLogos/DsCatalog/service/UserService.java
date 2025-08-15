package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.projections.UserProjection;
import com.RadixLogos.DsCatalog.entities.Role;
import com.RadixLogos.DsCatalog.entities.User;
import com.RadixLogos.DsCatalog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserProjection> userProjections = userRepository.findUserByUsername(username);
        var userData = userProjections.getFirst();
        User user = new User();
        user.setEmail(userData.getEmail());
        user.setPassword(userData.getPassword());
        for(Role r : userProjections.getFirst().getAuthorities()){
            Role role = new Role();
            role.setId(r.getId());
            role.setAuthority(r.getAuthority());
            user.addAuthority(role);
        }
        return user;
    }
}
