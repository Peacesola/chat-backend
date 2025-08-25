package com.peace.Chat.controller;

import com.peace.Chat.dto.AuthResponse;
import com.peace.Chat.dto.UserResponse;
import com.peace.Chat.model.User;
import com.peace.Chat.security.CustomUserDetailsService;
import com.peace.Chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/me")
    public ResponseEntity<Map<String,Object>> me(@AuthenticationPrincipal UserDetails principal) {
        try {
            var user = userService.findByEmail(principal.getUsername());
            var response= AuthResponse.builder()
                    .email(user.getEmail())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
            return ResponseEntity.ok(Map.of(
                    "message","User details fetched successfully",
                    "user",response
            ));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of(
                    "message","Failed to load information. Please check your internet connection",
                    "error",e.getMessage()  //long error msg: do not display
            ));
        }
    }

    @PostMapping("/me/profile-image")
    public ResponseEntity<Map<String,Object>>uploadProfileImage(
            //@PathVariable String id,
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        try {
            var user = userService.findByEmail(principal.getUsername());
            String url = userService.uploadProfileImage(user.getId(), file);
            return ResponseEntity.ok(Map.of(
                    "message","Photo uploaded successfully",
                    "url",url
            ));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of(
                    "error",e.getMessage()  //long error msg: do not display
            ));
        }
    }

}
