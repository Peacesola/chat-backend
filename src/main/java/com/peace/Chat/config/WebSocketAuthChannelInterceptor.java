package com.peace.Chat.config;

import com.peace.Chat.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService users;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Read token from native header: Authorization: Bearer <jwt>
            var token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                var raw = token.substring(7);
                try {
                    var username = jwtService.extractUsername(raw);
                    var user = users.loadUserByUsername(username);
                    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    accessor.setUser(auth); // now messages will carry Principal
                } catch (Exception ignored) { /* deny silently → client won't connect */ }
            }
        }
        return message;
    }
        }
