package com.peace.Chat.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("chats")
public class Chat {
    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private ChatType type;
    private String name;
    private List<String> participants;
    private Set<String> admins;

    private String lastMessageId;
    private Instant createdAt;
    private Instant updatedAt;

}
