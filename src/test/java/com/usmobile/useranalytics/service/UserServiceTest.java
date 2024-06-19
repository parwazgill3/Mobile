package com.usmobile.useranalytics.service;

import com.usmobile.useranalytics.dto.CreateUserDTO;
import com.usmobile.useranalytics.dto.UpdateUserDTO;
import com.usmobile.useranalytics.exception.EmailAlreadyInUseException;
import com.usmobile.useranalytics.exception.UserNotFoundException;
import com.usmobile.useranalytics.model.User;
import com.usmobile.useranalytics.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_success() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName("Parwaz");
        createUserDTO.setLastName("Gill");
        createUserDTO.setEmail("parwazgill3@gmail.com");
        createUserDTO.setPassword("parwaz123");

        User user = new User();
        user.setId("12345");
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setEmail(createUserDTO.getEmail());

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        CreateUserDTO result = userService.createUser(createUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals("12345", result.getId());
        assertEquals("Parwaz", result.getFirstName());
        assertEquals("Gill", result.getLastName());
        assertEquals("parwazgill3@gmail.com", result.getEmail());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertTrue(new BCryptPasswordEncoder().matches("parwaz123", userArgumentCaptor.getValue().getPassword()));
    }

    @Test
    void createUser_emailAlreadyInUse() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName("Parwaz");
        createUserDTO.setLastName("Gill");
        createUserDTO.setEmail("parwazgill3@gmail.com");
        createUserDTO.setPassword("parwaz123");

        User existingUser = new User();
        existingUser.setEmail("parwazgill3@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(EmailAlreadyInUseException.class, () -> {
            userService.createUser(createUserDTO);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_success() {
        // Arrange
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Arnaaz");
        updateUserDTO.setLastName("Gill");
        updateUserDTO.setEmail("arnaazgill@gmail.com");

        User existingUser = new User();
        existingUser.setId("12345");
        existingUser.setFirstName("Parwaz");
        existingUser.setLastName("Gill");
        existingUser.setEmail("parwazgill3@gmail.com");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        CreateUserDTO result = userService.updateUser("12345", updateUserDTO).orElse(null);

        // Assert
        assertNotNull(result);
        assertEquals("12345", result.getId());
        assertEquals("Arnaaz", result.getFirstName());
        assertEquals("Gill", result.getLastName());
        assertEquals("arnaazgill@gmail.com", result.getEmail());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals("Arnaaz", userArgumentCaptor.getValue().getFirstName());
        assertEquals("Gill", userArgumentCaptor.getValue().getLastName());
        assertEquals("arnaazgill@gmail.com", userArgumentCaptor.getValue().getEmail());
    }

    @Test
    void updateUser_notFound() {
        // Arrange
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Arnaaz");
        updateUserDTO.setLastName("Gill");
        updateUserDTO.setEmail("arnaazgill@gmail.com");

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser("12345", updateUserDTO);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_success() {
        // Arrange
        User user = new User();
        user.setId("12345");
        user.setFirstName("Parwaz");
        user.setLastName("Gill");
        user.setEmail("parwazgill3@gmail.com");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Act
        Optional<CreateUserDTO> result = userService.getUserById("12345");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("12345", result.get().getId());
        assertEquals("Parwaz", result.get().getFirstName());
        assertEquals("Gill", result.get().getLastName());
        assertEquals("parwazgill3@gmail.com", result.get().getEmail());
    }

    @Test
    void getUserById_notFound() {
        // Arrange
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<CreateUserDTO> result = userService.getUserById("12345");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updateUser_emailAlreadyInUse() {
        // Arrange
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Arnaaz");
        updateUserDTO.setLastName("Gill");
        updateUserDTO.setEmail("arnaazgill@gmail.com");

        User existingUser = new User();
        existingUser.setId("12345");
        existingUser.setFirstName("Parwaz");
        existingUser.setLastName("Gill");
        existingUser.setEmail("parwazgill3@gmail.com");

        User anotherUser = new User();
        anotherUser.setId("67890");
        anotherUser.setEmail("arnaazgill@gmail.com");

        when(userRepository.findById("12345")).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("arnaazgill@gmail.com")).thenReturn(Optional.of(anotherUser));

        // Act & Assert
        assertThrows(EmailAlreadyInUseException.class, () -> {
            userService.updateUser("12345", updateUserDTO);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_withInvalidData() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO(); // Missing required fields

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(createUserDTO);
        });

        verify(userRepository, never()).save(any(User.class));
    }

}

