package com.example.conference.services;

import com.example.conference.dto.UserDTO;
import com.example.conference.entity.User;
import com.example.conference.entity.enums.ERole;
import com.example.conference.exceptions.UserExistException;
import com.example.conference.payload.request.SignupRequest;
import com.example.conference.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_LISTENER);

        try {
            LOG.info("Saving User {}", userIn.getEmail());
            userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());

        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }

    public void createUserByAdmin(SignupRequest userIn, Principal principal) {
        User newuser = new User();

        if (getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)) {
            User user = new User();
            user.setEmail(userIn.getEmail());
            user.setName(userIn.getFirstname());
            user.setLastname(userIn.getLastname());
            user.setUsername(userIn.getUsername());
            user.setPassword(passwordEncoder.encode(userIn.getPassword()));
            user.getRoles().add(ERole.ROLE_LISTENER);

            newuser = user;
        }
        try {
            userRepository.save(newuser);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + newuser.getUsername() + " already exist. Please check credentials");
        }
    }

    public User updateUserById(String userId, UserDTO userDTO, Principal principal) {
        User updateduser = new User();
        if (getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)) {

            User user = getUserById(Long.parseLong(userId));
            user.setName(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());

            updateduser = user;
        }
        return userRepository.save(updateduser);
    }

    public void deleteUserById(long userId, Principal principal) {
        User deleteduser = new User();
        if (getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)) {
            deleteduser = getUserById(userId);
        }
        userRepository.delete(deleteduser);
    }

    public void setRoleSpeakerById(long userId, Principal principal) {
        User updateduser = new User();
        if (getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_ADMIN)) {
            Set<ERole> roles = new HashSet<>();
            roles.add(ERole.ROLE_LISTENER);
            roles.add(ERole.ROLE_SPEAKER);

            User user = getUserById(userId);
            user.setRoles(roles);

            updateduser = user;
        }
        userRepository.save(updateduser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}