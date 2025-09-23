package com.peace.Chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String profileImageUrl;
    private String fcmToken;
}
