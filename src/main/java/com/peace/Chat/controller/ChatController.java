package com.peace.Chat.controller;

import com.peace.Chat.dto.CreateChatRequest;
import com.peace.Chat.dto.SendMessageRequest;
import com.peace.Chat.model.Chat;
import com.peace.Chat.model.Message;
import com.peace.Chat.model.MessageType;
import com.peace.Chat.repo.MessageRepository;
import com.peace.Chat.service.ChatService;
import com.peace.Chat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messages;
    private final SimpMessagingTemplate broker;
    private final ChatService chats;
    private MessageRepository repository;


    @MessageMapping("/chat.send")
    public void handleSend(@Payload SendMessageRequest req/*, @AuthenticationPrincipal UserDetails me*/) {

        //String senderEmail = me.getUsername();

        var savedMessage= messages.sendMessage(req.getChatId(),req.getSenderId(),req.getReceiverId(),req.getContent());
        String destination = "/topic/chats/" + req.getChatId();
        broker.convertAndSend(destination, savedMessage);

        System.out.println("message:"+req);
        // Notify the receiver
       // broker.convertAndSendToUser(senderEmail,"/queue/messages",savedMessage);

        // Notify the sender (for confirmation)
        /*broker.convertAndSendToUser(
                senderUsername,
                "/queue/messages",
                saved
        );*/
    }


    @MessageMapping("/chat.typing")
    public void typing(@Header("chatId") String chatId, Authentication auth) {
        broker.convertAndSend("/topic/typing." + chatId, auth.getName());
    }


    @GetMapping("/{chatId}/messages")
    public ResponseEntity<Map<String,Object>> history(
            @PathVariable String chatId
    ) {
        var message= messages.history(chatId/*, page, size*/);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message","Messages fetched successfully",
                "messages",message
        ));
    }



    @PostMapping
    public ResponseEntity<Map<String,Object>> createChat(
            @RequestBody @Valid
             CreateChatRequest req
            // @AuthenticationPrincipal UserDetails me
    ) {
        var chatRoom = chats.createChat( req.getSenderId(), req.getReceiverId());
        return ResponseEntity.ok(Map.of(
                "message","Chat created successfully",
                "chatId",chatRoom
        ));
    }

    @PutMapping("/{chatId}/messages/{id}")
    public ResponseEntity<Map<String,Object>> editMessage(
         @RequestBody SendMessageRequest req,
         @PathVariable String id
    ){
        Message message= repository.findById(id).orElseThrow();
        if(req.getSenderId().equals(message.getSenderId())){
            message.setContent(req.getContent());
           var updatedMessage= repository.save(message);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message","Message updated successfully",
                    "data",updatedMessage
            ));
        }
       else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message","Failed to update message"
        ));
    }
}
