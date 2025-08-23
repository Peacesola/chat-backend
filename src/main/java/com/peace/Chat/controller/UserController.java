package com.peace.Chat.controller;

import com.peace.Chat.dto.AuthResponse;
import com.peace.Chat.dto.UserResponse;
import com.peace.Chat.model.User;
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

    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal UserDetails principal) {
        var user = userService.findByUsername(principal.getUsername());
        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .profileImageUrl(user.getProfileImageUrl()!=null?user.getProfileImageUrl():null )
                .build();
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<Map<String,Object>>uploadProfileImage(
            @PathVariable String id,
            @RequestParam("file")MultipartFile file
            ) throws IOException {
        try {
            String url = userService.uploadProfileImage(id, file);
            return ResponseEntity.ok(Map.of(
                    "message","Photo uploaded successfully",
                    "url",url
            ));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of(
                    "message",e.getMessage()  //long error msg: do not display
            ));
        }
    }

}
