package com.peace.Chat.dto;

import com.peace.Chat.model.ChatType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class CreateChatRequest {
    /*private ChatType type;
    @NotEmpty
    private List<String> participantUserIds;
    private String name;*/
    private String senderId;
    private String receiverId;
}
