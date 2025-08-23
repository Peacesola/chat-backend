package com.peace.Chat.dto;

import com.peace.Chat.model.ChatType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class ChatSummary {
    private String id;
    private ChatType type;
    private String name;
    private List<String> participants;
    private String lastMessagePreview;

}
