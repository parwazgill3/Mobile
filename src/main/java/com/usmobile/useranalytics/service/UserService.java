package com.usmobile.useranalytics.service;

import com.usmobile.useranalytics.dto.CreateUserDTO;
import com.usmobile.useranalytics.dto.UpdateUserDTO;
import com.usmobile.useranalytics.exception.EmailAlreadyInUseException;
import com.usmobile.useranalytics.exception.UserNotFoundException;
import com.usmobile.useranalytics.model.User;
import com.usmobile.useranalytics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public CreateUserDTO createUser(CreateUserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }
        User user = CreateUserDTO.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        return CreateUserDTO.fromUser(userRepository.save(user));
    }

    public Optional<CreateUserDTO> updateUser(String id, UpdateUserDTO userDTO) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent() && !existingUser.get().getEmail().equals(userDTO.getEmail())) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }

        return existingUser.map(user -> {
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            return CreateUserDTO.fromUser(userRepository.save(user));
        });
    }

    public Optional<CreateUserDTO> getUserById(String id) {
        return userRepository.findById(id).map(CreateUserDTO::fromUser);
    }
}
