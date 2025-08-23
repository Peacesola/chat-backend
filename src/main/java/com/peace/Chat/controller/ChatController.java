package com.peace.Chat.controller;

import com.peace.Chat.dto.SendMessageRequest;
import com.peace.Chat.model.Message;
import com.peace.Chat.model.MessageType;
import com.peace.Chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Date;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final MessageService messages;
    private final SimpMessagingTemplate broker;


    @MessageMapping("/chat.send")
    public void handleSend(@Payload SendMessageRequest req, Authentication auth) {

        String senderUsername = auth.getName();

        var saved = messages.sendMessage(req.getChatId(), senderUsername,
                /*req.getType() == null ? MessageType.TEXT : req.getType(),*/ req.getContent());

        String destination = "/topic/chats." + saved.getChatId();
        broker.convertAndSend(destination, saved);
    }


    @MessageMapping("/chat.typing")
    public void typing(@Header("chatId") String chatId, Authentication auth) {
        broker.convertAndSend("/topic/typing." + chatId, auth.getName());
    }
}
