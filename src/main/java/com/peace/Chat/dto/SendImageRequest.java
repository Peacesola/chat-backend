package com.peace.Chat.dto;

import com.peace.Chat.model.MessageType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class SendImageRequest {
    @NotBlank
    private String chatId;
    private MessageType type = MessageType.IMAGE;
    @NotBlank
    private String content;
}
