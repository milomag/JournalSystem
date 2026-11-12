package com.journalsystem.backend.dto.request;


import jakarta.validation.constraints.NotBlank;

public class MessageRequest{

    @NotBlank
    private String content;

    public MessageRequest(){}

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}
