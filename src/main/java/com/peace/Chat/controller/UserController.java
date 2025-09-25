package com.peace.Chat.controller;

import com.peace.Chat.dto.AuthResponse;
import com.peace.Chat.dto.FcmRequest;
import com.peace.Chat.dto.RegisterResponse;
import com.peace.Chat.dto.UserResponse;
import com.peace.Chat.model.User;
import com.peace.Chat.repo.UserRepository;
import com.peace.Chat.security.CustomUserDetailsService;
import com.peace.Chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllUsers(){
        var users= userService.getAllUsers();
        try{
            if(users.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                        "message","No users"
                ));
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message","Users fetched successfully",
                    "users",users
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message","Could not fetch users"
            ));
        }
    }


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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message","Failed to load information. Please check your internet connection",
                    "error",e.getMessage()  //long error msg: do not display
            ));
        }
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<Map<String,Object>>uploadProfileImage(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        try {
            String url = userService.uploadProfileImage(id, file);
            return ResponseEntity.ok(Map.of(
                    "message","Photo uploaded successfully",
                    "url",url
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error",e.getMessage()  //long error msg: do not display
            ));
        }
    }

    @PostMapping("/{id}/fcm-token")
    public ResponseEntity<Map<String, Object>>updateFcmToken(
            @PathVariable String id,
            @RequestBody FcmRequest request
            ){
        try {
            String fcmToken =request.getFcmToken();
            String fcm = userService.saveFcmToken(id,fcmToken);
            /*var user= userRepository.findById(id).orElseThrow();
            String fcmToken = body.get("fcmToken");
            user.setFcmToken(fcmToken);
            userRepository.save(user);*/

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "FCM token updated",
                    "fcm",fcm
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "FCM token update failed"
            ));
        }
    }

}
