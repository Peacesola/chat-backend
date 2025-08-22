package com.peace.Chat.controller;

import com.peace.Chat.dto.CreateChatRequest;
import com.peace.Chat.dto.SendImageRequest;
import com.peace.Chat.dto.SendMessageRequest;
import com.peace.Chat.model.Message;
import com.peace.Chat.service.ChatService;
import com.peace.Chat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chats;
    private final MessageService messages;


    @PostMapping
    public ResponseEntity<Map<String,Object>> create(@RequestBody @Valid CreateChatRequest req,
                         @AuthenticationPrincipal UserDetails me) {
        var chat = chats.createChat(req.getType(), req.getParticipantUserIds(), req.getName());
        return ResponseEntity.ok(Map.of(
                "message","Chat created successfully: "+chat
        ));
    }

    @GetMapping
    public Object myChats(@AuthenticationPrincipal UserDetails me) {
        return chats.listForUser(me.getUsername());
    }

    @GetMapping("/{chatId}/messages")
    public Object history(@PathVariable String chatId,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "30") int size) {
        return messages.history(chatId, page, size);
    }


    @PostMapping("/send_text")
    public ResponseEntity<Map<String,Object>> sendMessage(
            @RequestBody @Valid SendMessageRequest req,
            @AuthenticationPrincipal UserDetails me
    ) {
        var sentMessage= messages.sendMessage(req.getChatId(), me.getUsername(), /*req.getType(),*/ req.getContent());

        return ResponseEntity.ok(Map.of(
                "message","Message sent successfully: "+sentMessage
        ));
    }

    @PostMapping("/{senderId}/image")
    public ResponseEntity<Map<String,Object>> sendImage(
            @RequestBody @Valid SendImageRequest req,
            @PathVariable String senderId,
            @RequestParam("file") MultipartFile file) {
        try {
            var message = messages.sendImage(senderId,req.getChatId(),file);
            return ResponseEntity.ok(Map.of(
                    "message","Image sent successfully: "+message
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message","Failed to send image:"+ e.getMessage()
            ));
        }
    }

}
