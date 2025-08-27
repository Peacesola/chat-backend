package com.peace.Chat.dto;

import com.peace.Chat.model.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class SendMessageRequest {
    @NotBlank
    private String chatId;
    private MessageType type = MessageType.TEXT;
    @NotBlank
    private String content;
    private String senderId;
    private String receiverId;
}
