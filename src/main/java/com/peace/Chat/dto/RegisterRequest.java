package com.peace.Chat.dto;

import com.peace.Chat.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Set;

@RequiredArgsConstructor
@Data
public class RegisterRequest {

    @NotBlank
    @Size(min=3, max=30)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=8, max=100)
    private String password;

}


