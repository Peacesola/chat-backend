package com.peace.Chat.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.peace.Chat.model.NotificationMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(NotificationMessage notificationMessage){
        try {
            Message message= Message.builder()
                    .setNotification(Notification.builder()
                            .setBody(notificationMessage.getBody())
                            .setTitle(notificationMessage.getTitle()).build())
                    .setToken(notificationMessage.getToken())
                    .build();

            String response= FirebaseMessaging.getInstance().send(message);
            System.out.println("Notification sent successfully: " + response);
        }catch (Exception e){
           e.printStackTrace();
        }

    }
}
