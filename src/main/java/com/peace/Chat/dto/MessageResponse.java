package com.peace.Chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Data@Builder
public class MessageResponse {
    private String id;
    private String senderId;
    private String recipientId;
    private String content;
    private Instant createdAt;
}
