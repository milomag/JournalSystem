package com.journalsystem.backend.dto.response;

import java.time.LocalDateTime;

public class MessageResponse{

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;

    public MessageResponse(
            Long id,
            Long senderId,
            Long receiverId,
            String content,
            LocalDateTime timestamp){

        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId(){
        return id;
    }

    public Long getSenderId(){
        return senderId;
    }

    public Long getReceiverId(){
        return receiverId;
    }

    public String getContent(){
        return content;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }
}

