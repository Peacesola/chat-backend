package com.peace.Chat.service;

import com.peace.Chat.model.Chat;
import com.peace.Chat.model.ChatType;
import com.peace.Chat.repo.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepository chats;

    public String generateChatId (String senderId, String receiverId){
       /* if(senderId.compareTo(receiverId)<0){
            return senderId+"_"+receiverId;
        }else {
            return receiverId+"_"+senderId;
        }*/return Stream.of(senderId, receiverId)
                .sorted()
                .collect(Collectors.joining("_"));
    }

    public Chat createChat(/*List<String> participantUserIds*/ String senderId, String receiverId) {
        var now = Instant.now();
        String id= generateChatId(senderId,receiverId);
        var chat = Chat.builder()
                .id(id)
                .senderId(senderId)
                .receiverId(receiverId)
                //.type(type)
                //.name(type == ChatType.GROUP ? name : null)
                //.participants(participantUserIds)
                //.admins(type == ChatType.GROUP ? Set.copyOf(participantUserIds.subList(0,1)) : null)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return chats.save(chat);
    }

    public List<Chat> listForUser(String userId) {
        return chats.findByParticipantsContaining(userId);
    }
}
