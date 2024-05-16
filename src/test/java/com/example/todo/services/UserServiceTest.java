package com.example.todo.services;


import com.example.todo.dtos.RegistrationUserDto;
import com.example.todo.dtos.UserDto;
import com.example.todo.models.User;
import com.example.todo.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findByUsername() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername(username);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void loadUserByUsername() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("user")));
    }

    @Test
    void loadUserByUsernameUserNotFound() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void createNewUser() {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setName(registrationUserDto.getName());
        user.setPassword("encoded_password");

        when(passwordEncoder.encode(registrationUserDto.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createNewUser(registrationUserDto);

        assertEquals(user, createdUser);
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(registrationUserDto.getPassword());
    }

    @Test
    void getAuthenticatedUser() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password", List.of(new SimpleGrantedAuthority("user")));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User authenticatedUser = userService.getAuthenticaticatedUser();

        assertEquals(user, authenticatedUser);
    }

    @Test
    void updateAuthenticatedUserWithoutUsername() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setName("Test User");

        UserDto userDto = new UserDto(1L, "username", "name");
        userDto.setUsername("new_username"); // trying to change username
        userDto.setName("Updated User");

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password", List.of(new SimpleGrantedAuthority("user")));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateAuthenticatedUser(userDto);

        assertEquals("Test User", updatedUser.getName());
        assertEquals(username, updatedUser.getUsername());
        verify(userRepository).save(any(User.class));
    }
}