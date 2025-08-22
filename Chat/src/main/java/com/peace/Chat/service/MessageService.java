package com.peace.Chat.service;

import com.peace.Chat.model.Message;
import com.peace.Chat.model.MessageType;
import com.peace.Chat.repo.MessageRepository;
import io.jsonwebtoken.io.IOException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messages;

    private CloudinaryService cloudinaryService;

    public Message sendMessage(String chatId, String senderId/*, MessageType type,*/ ,String content) throws IOException{
        var msg = Message.builder()
                .chatId(chatId)
                .senderId(senderId)
                .type(MessageType.TEXT)
                .content(content)
                .sentAt(Instant.now())
                .deliveredTo(Set.of(senderId))
                .readBy(Set.of(senderId))
                .build();
        return messages.save(msg);
    }

    public Message sendImage(String chatId, String senderId,MultipartFile file) throws IOException, java.io.IOException {

        String imageUrl= cloudinaryService.upLoadFile(file, "chat_app/messages");

        var msg = Message.builder()
                .chatId(chatId)
                .senderId(senderId)
                .type(MessageType.IMAGE)
                .content(imageUrl)
                .sentAt(Instant.now())
                .deliveredTo(Set.of(senderId))
                .readBy(Set.of(senderId))
                .build();
        return messages.save(msg);
    }

    public List<Message> history(String chatId, int page, int size) {
        return messages.findByChatIdOrderBySentAtDesc(chatId, PageRequest.of(page, size));
    }
}
