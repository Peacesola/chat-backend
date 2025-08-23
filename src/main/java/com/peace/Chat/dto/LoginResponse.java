package com.peace.Chat.dto;

import com.peace.Chat.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;


@AllArgsConstructor
@Builder
@Data
public class LoginResponse {

    private String token;
    private  String username;
    private String email;
    //private  String displayName;
    private  String userId;
   /* private Set<Role> roles;*/
}
