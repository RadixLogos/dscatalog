package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.RoleDTO;
import com.RadixLogos.DsCatalog.dto.UserDTO;
import com.RadixLogos.DsCatalog.dto.UserInsertDTO;
import com.RadixLogos.DsCatalog.dto.projections.UserProjection;
import com.RadixLogos.DsCatalog.entities.Role;
import com.RadixLogos.DsCatalog.entities.User;
import com.RadixLogos.DsCatalog.repositories.RoleRepository;
import com.RadixLogos.DsCatalog.repositories.UserRepository;
import com.RadixLogos.DsCatalog.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserProjection> userProjections = userRepository.findUserByUsername(username);
        if(userProjections.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        var userData = userProjections.getFirst();
        User user = new User();
        user.setEmail(userData.getEmail());
        user.setPassword(userData.getPassword());
       for(UserProjection u : userProjections){
           Role role = new Role(u.getRoleId(),u.getAuthority());
           user.addAuthority(role);
       }
        return user;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllUsersPaged(Pageable pageable){
        var users = userRepository.findAll(pageable);
        return users.map(UserDTO::fromUser);
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id){
        var user = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User not found"));
        return UserDTO.fromUser(user);
    }

    @Transactional
    public UserDTO insertUser(UserInsertDTO userInsertDTO){
        var user = new User();
        copyDTOToEntity(userInsertDTO.userDTO(), user);
        user.setPassword(encoder.encode(userInsertDTO.password()));
        user.getAuthorities().clear();
        user.addAuthority(roleRepository.findRoleByAuthority("ROLE_OPERATOR"));
        userRepository.save(user);
        return UserDTO.fromUser(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO){
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User not found!");
        }
        var user =  userRepository.getReferenceById(id);
        copyDTOToEntity(userDTO,user);
        userRepository.save(user);
        return UserDTO.fromUser(user);
    }

    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User not found!");
        }
        userRepository.deleteById(id);
    }

    private void copyDTOToEntity(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setEmail(userDTO.email());
        user.getAuthorities().clear();
        if(userDTO.roles() != null){
            userDTO.roles().forEach(r ->{
                var role = findRole(r);
                var authority = new Role(role.getId(), role.getAuthority());
                user.addAuthority(authority);
            });
        }
    }

    private Role findRole(RoleDTO r) {
        if(!roleRepository.existsById(r.id())){
            throw new NotFoundException("Authority not found!");
        }
        return roleRepository.getReferenceById(r.id());
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findUserByEmail(username);
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

    @Transactional
    public UserDTO findMe() {
        return UserDTO.fromUser(authenticated());
    }
}

