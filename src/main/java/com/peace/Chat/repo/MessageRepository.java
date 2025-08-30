package com.peace.Chat.repo;

import com.peace.Chat.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message,String> {

    List<Message> findByChatIdOrderBySentAtDesc(String chatId/*, Pageable page*/);
}
