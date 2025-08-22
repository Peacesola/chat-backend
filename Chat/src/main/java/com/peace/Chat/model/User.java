package com.peace.Chat.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;
import java.time.Instant;
import java.util.Set;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    //@Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    //private String displayName;

    private String profileImageUrl;

    private String password;


   /* private String statusMessage;

    private Instant createdAt;

    private Instant lastSeen;*/

    private Set<Role> roles;

}
