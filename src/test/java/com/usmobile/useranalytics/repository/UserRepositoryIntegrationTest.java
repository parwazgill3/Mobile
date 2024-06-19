package com.usmobile.useranalytics.repository;

import com.usmobile.useranalytics.repository.BaseIntegrationTest;
import com.usmobile.useranalytics.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateUser() {
        User user = new User("testId", "Test", "User", "testuser@gmail.com", "password");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById("testId");
        assertTrue(foundUser.isPresent());
        assertEquals("Test", foundUser.get().getFirstName());
        assertEquals("User", foundUser.get().getLastName());
        assertEquals("testuser@gmail.com", foundUser.get().getEmail());
    }

    @Test
    void testUpdateUser() {

        Optional<User> foundUser = userRepository.findById("12345");
        assertTrue(foundUser.isPresent());

        foundUser.get().setEmail("parwazgill@gmail.com");
        userRepository.save(foundUser.get());

        Optional<User> updatedUser = userRepository.findById("12345");
        assertTrue(updatedUser.isPresent());
        assertEquals("parwazgill@gmail.com", updatedUser.get().getEmail());
    }

    @Test
    void testGetUserById() {
        Optional<User> foundUser = userRepository.findById("12345");
        assertTrue(foundUser.isPresent());
        assertEquals("Parwaz", foundUser.get().getFirstName());
    }

    @Test
    void testGetUserById_NotFound() {
        Optional<User> foundUser = userRepository.findById("nonExistentId");
        assertFalse(foundUser.isPresent());
    }
}
