package com.peace.Chat.repo;

import com.peace.Chat.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByParticipantsContaining(String userId);
}
