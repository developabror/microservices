package uz.app.authapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.app.authapp.entity.User;
import uz.app.authapp.entity.enums.Role;
import uz.app.authapp.exceptions.DublicateUserException;
import uz.app.authapp.payload.JwtProvider;
import uz.app.authapp.payload.ResponseMessage;
import uz.app.authapp.payload.SignInDTO;
import uz.app.authapp.payload.UserDTO;
import uz.app.authapp.repository.UserRepository;

import java.util.Optional;

@RestController

@RequestMapping("/auth")
public class AuthController {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtProvider jwtProvider;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok("auth app is available");
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity
                    .status(409)
                    .body(new ResponseMessage(false, "user already exists", userDTO.getUsername()));
        }
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Role role = Role.valueOf(userDTO.getRole());
        user.setRole(role);
        user.setActive(true);
        userRepository.save(user);
        return ResponseEntity.status(201).body(
                new ResponseMessage(true, "User created successfully", user)
        );
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO) {
        Optional<User> optionalUser = userRepository.findByUsername((signInDTO.getUsername()));
        if (optionalUser.isEmpty())
            return ResponseEntity
                    .status(409)
                    .body(new ResponseMessage(false, "user not found", signInDTO.getUsername()));

        User user = optionalUser.get();
        if (!passwordEncoder.matches(signInDTO.getPassword(),user.getPassword())){
            return ResponseEntity
                    .status(409)
                    .body(new ResponseMessage(false, "password not match", signInDTO.getPassword()));

        }
        return ResponseEntity.ok(new ResponseMessage(true,"successfully signed in",jwtProvider.generateToken(user.getUsername())));

    }
}
