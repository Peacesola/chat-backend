package com.peace.Chat.controller;


import com.peace.Chat.dto.*;
import com.peace.Chat.model.Role;
import com.peace.Chat.model.User;
import com.peace.Chat.repo.UserRepository;
import com.peace.Chat.security.JwtService;
import com.peace.Chat.service.AuthService;

import com.peace.Chat.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository user;
    private final PasswordEncoder encoder;
    private final AuthService authService;
    private final AuthenticationManager manager;


    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@RequestBody @Valid RegisterRequest request) {
        if (user.existsByEmail(request.getEmail()))
            return ResponseEntity.badRequest().body(Map.of(
                    "message","Email already in use"
            ));
        if (request.getUsername()==null || request.getEmail()==null || request.getPassword()==null)
            return ResponseEntity.badRequest().body(Map.of(
                    "message","All fields are required"
            ));
        User u= User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .roles(Set.of(Role.USER))
                .build();

        user.save(u);

        var email= u.getEmail();
        var response= RegisterResponse.builder()
                .userId(u.getId())
                .username(u.getUsername())
                .email(email)
                .build();

        return  ResponseEntity.ok(Map.of(
                "message","User registered successfully",
                "user",response
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody @Valid LoginRequest request){
        try {
            /*Authentication auth =*/ manager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

      /*  var principal = (UserDetails) auth.getPrincipal();
        String token = jwt.generateToken(principal.getUsername(), Map.of("roles", principal.getAuthorities()));
        var user= authService.login(req);
        return ResponseEntity.ok(user);*/

            User u = user.findByEmail(request.getEmail()).orElseThrow();

            var id= u.getId();
            var email= u.getEmail();
            String token = jwtService.generateToken(u.getEmail(), Map.of("uid",id ));

            var response= LoginResponse.builder()
                    .token(token)
                    //.expiresInSec(60L * 60L)
                    .userId(id)
                    .username(u.getUsername())
                    .email(email)
                    .build();
            if (user.existsByEmail(request.getEmail())){
                return  ResponseEntity.ok(Map.of(
                        "message","User logged in successfully",
                        "user",response
                ));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "User not found"
                ));
            }

        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "Invalid email or password"
            ));
        }/*catch (UsernameNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "User not found"
            ));
        }*/
    }



}
