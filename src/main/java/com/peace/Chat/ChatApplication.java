package com.peace.Chat;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;

@SpringBootApplication
@RestController
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}


	@Value("${app.jwt.secret-base64}")
	private String secretBase64;

	@Value("${app.jwt.exp-minutes}")
	private String expirationMinutes;



}
