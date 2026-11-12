
package com.journalsystem.backend.entity;

import com.journalsystem.backend.dto.response.MessageResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Size(max = 2000)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Message(){}

    @PrePersist
    protected void onCreate(){
        timestamp = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId(){
        return id;
    }

    public User getSender(){
        return sender;
    }

    public void setSender(User sender){
        this.sender = sender;
    }

    public User getReceiver(){
        return receiver;
    }

    public void setReceiver(User receiver){
        this.receiver = receiver;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public MessageResponse toMessageResponse(){
        return new MessageResponse(
                id,
                sender.getId(),
                receiver.getId(),
                content,
                timestamp
        );
    }
}
