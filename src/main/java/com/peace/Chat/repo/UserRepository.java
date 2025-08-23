package com.peace.Chat.repo;

import com.peace.Chat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    //Optional<User> findById(String id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
