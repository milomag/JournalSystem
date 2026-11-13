
package com.journalsystem.backend.controller;

import com.journalsystem.backend.dto.request.MessageRequest;
import com.journalsystem.backend.dto.response.MessageResponse;
import com.journalsystem.backend.dto.response.UserResponse;
import com.journalsystem.backend.service.MessageService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController{

    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<MessageResponse>> getConversation(
            @PathVariable Long userId,
            Authentication authentication){

        List<MessageResponse> messages = messageService.getConversation(authentication.getName(), userId);

        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable Long userId,
            @Valid @RequestBody MessageRequest request,
            Authentication authentication){

        MessageResponse response = messageService.sendMessage(authentication.getName(), userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<UserResponse>> getContacts(Authentication authentication){

        List<UserResponse> contacts = messageService.getContacts(authentication.getName());

        return ResponseEntity.ok(contacts);
    }
}
