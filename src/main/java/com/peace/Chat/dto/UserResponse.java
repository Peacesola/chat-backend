package com.peace.Chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
    @Builder
    @Data
    public class UserResponse {
        private String id;
        private String username;
        private String email;
        private String profileImageUrl;
       // private String displayName;
    }
