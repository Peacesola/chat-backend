package com.peace.Chat.service;


import com.peace.Chat.dto.*;
import com.peace.Chat.model.Role;
import com.peace.Chat.model.User;
import com.peace.Chat.repo.UserRepository;
import com.peace.Chat.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository user;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public ResponseEntity<Map<String,Object>> register(RegisterRequest request) throws BadRequestException {
        /*if (user.existsByUsername(request.getUsername()))
            throw new  BadRequestException("Username already taken");*/
         if (user.existsByEmail(request.getEmail()))
             //throw new BadRequestException("Email already registered");
             return ResponseEntity.badRequest().body(Map.of(
                     "message","Username already taken"
             ));
        User u= User.builder()
                .id(request.g)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .roles(Set.of(Role.USER))
                .build();

        user.save(u);

        //String token= jwtService.generateToken(u.getUsername(), Map.of("uid",u.getId()));
        var email= u.getEmail();
        var response= RegisterResponse.builder()
                //.token(token)
                //.expiresInSec(60L * 60L)
                //.userId(u.getId())
                .username(u.getUsername())
                .email(email)
                .build();

        return  ResponseEntity.ok(Map.of(
                "message","User registered successfully",
                "user",response
        ));
    }

    public ResponseEntity<Map<String,Object>> login(LoginRequest request) throws BadRequestException{
        var authToken= new UsernamePasswordAuthenticationToken(request.getEmail(),encoder.encode(request.getPassword()));
        manager.authenticate(authToken);
        User u = user.findByEmail(request.getEmail()).orElseThrow();

        /*if (u.getLastLoginToken() != null) {
            throw new BadRequestException("User is already logged in");
        }*/

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

        return  ResponseEntity.ok(Map.of(
                "message","User logged in successfully",
                "user",response
        ));

    }

}
