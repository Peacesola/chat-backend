package com.peace.Chat.controller;

import com.peace.Chat.dto.CreateChatRequest;
import com.peace.Chat.dto.SendMessageRequest;
import com.peace.Chat.model.Chat;
import com.peace.Chat.model.Message;
import com.peace.Chat.model.MessageType;
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


@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messages;
    private final SimpMessagingTemplate broker;
    private final ChatService chats;



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
           /* @PathVariable String chatId,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "30") int size*/
            @PathVariable String chatId
            //@RequestParam String receiverId
    ) {
        var message= messages.history(chatId/*, page, size*/);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message","Messages fetched successfully",
                "messages",message
        ));
    }

    /*@GetMapping("/private/messages")
    public Object privateHistory(){
        
    }*/

    @PostMapping
    public ResponseEntity<Map<String,Object>> createChat(
            @RequestBody @Valid
             CreateChatRequest req
            // @AuthenticationPrincipal UserDetails me
    ) {
        //var chatRoom= messages.generateChatId(req.getSenderId(), req.getReceiverId());
        var chatRoom = chats.createChat( req.getSenderId(), req.getReceiverId());
        return ResponseEntity.ok(Map.of(
                "message","Chat created successfully",
                "chatId",chatRoom
        ));
    }
}
