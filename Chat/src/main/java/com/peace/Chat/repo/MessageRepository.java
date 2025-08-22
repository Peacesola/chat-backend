package com.peace.Chat.repo;

import com.peace.Chat.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message,String> {
   /* @Query(value = "{ $or: [ { $and: [ { 'senderId': ?0 }, { 'recipientId': ?1 } ] }, { $and: [ { 'senderId': ?1 }, { 'recipientId': ?0 } ] } ] }",
            sort = "{ 'createdAt': 1 }")*/
    List<Message> findByChatIdOrderBySentAtDesc(String chatId, Pageable page);
}
