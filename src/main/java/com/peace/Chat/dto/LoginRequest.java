package com.peace.Chat.dto;

import com.peace.Chat.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Data
public class LoginRequest {
    @Email
    private String email;
    @NotBlank private String password;
}


