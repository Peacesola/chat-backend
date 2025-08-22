package com.peace.Chat.dto;


import com.peace.Chat.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@AllArgsConstructor
@Builder
@Data
public class AuthResponse {

    private String token;
    private  String username;
    private  String displayName;
    private  String userId;
    private  Set<Role> roles;
}
