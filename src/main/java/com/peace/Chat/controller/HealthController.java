package com.peace.Chat.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String,Object>> health(){
        return ResponseEntity.ok().body(Map.of(
                "message","OK"
        ));
    }
}