package com.peace.Chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Document(collection = "messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

   // @Id
    private String id;
    private String chatId;
    private String senderId;
    private String receiverId;
    private MessageType type; // TEXT/IMAGE/FILE
    private String content;   // text or URL for media

    private Instant sentAt;

   // private Set<String> deliveredTo; // user ids
   // private Set<String> readBy;
}
