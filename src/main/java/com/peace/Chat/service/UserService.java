package com.peace.Chat.service;

import com.peace.Chat.dto.UserDto;
import com.peace.Chat.dto.UserResponse;
import com.peace.Chat.model.Role;
import com.peace.Chat.model.User;
import com.peace.Chat.repo.UserRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CloudinaryService cloudinaryService;

    private final UserRepository userRepository;
    //private final PasswordEncoder encoder;

   /* public User register(String username, String displayName, String rawPassword) {
        if (users.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        var now = Instant.now();
        var user = User.builder()
                .username(username)
                .displayName(displayName)
                .password(encoder.encode(rawPassword)) // hash password
                .roles(Set.of(Role.USER))
                .createdAt(now)
               .lastSeen(now)
                .build();
        return users.save(user);
    }*/

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    public List<UserDto>getAllUsers(){
        List<User> users= userRepository.findAll();
        return users.stream().map(user -> new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getFcmToken()
        )).toList();
    }

    public String uploadProfileImage(String id, MultipartFile file) throws IOException, java.io.IOException {
        String imageUrl = cloudinaryService.upLoadFile(file, "chat_app/profiles");

        User user= userRepository.findById(id).orElseThrow();
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }
}
