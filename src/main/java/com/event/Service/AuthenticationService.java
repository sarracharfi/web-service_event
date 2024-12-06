package com.event.Service;

import com.event.dtos.LoginUserDto;
import com.event.dtos.RegisterUserDto;
import com.event.Model.Role;
import com.event.Model.User;
import com.event.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        if (userRepository.findByUsername(input.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        if (input.getRole() != Role.USER && input.getRole() != Role.ORGANIZER) {user.setRole(Role.USER);} else {
        user.setRole(input.getRole( ));
        }

        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
