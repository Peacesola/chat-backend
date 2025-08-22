package com.peace.Chat.service;

import com.peace.Chat.model.Chat;
import com.peace.Chat.model.ChatType;
import com.peace.Chat.repo.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepository chats;

    public Chat createChat(ChatType type, List<String> participantUserIds, String name) {
        var now = Instant.now();
        var chat = Chat.builder()
                .type(type)
                .name(type == ChatType.GROUP ? name : null)
                .participants(participantUserIds)
                .admins(type == ChatType.GROUP ? Set.copyOf(participantUserIds.subList(0,1)) : null)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return chats.save(chat);
    }

    public List<Chat> listForUser(String userId) {
        return chats.findByParticipantsContaining(userId);
    }
}
