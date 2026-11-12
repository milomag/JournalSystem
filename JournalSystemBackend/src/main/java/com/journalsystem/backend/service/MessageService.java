package com.journalsystem.backend.service;

import com.journalsystem.backend.dto.request.MessageRequest;
import com.journalsystem.backend.dto.response.MessageResponse;
import com.journalsystem.backend.dto.response.UserResponse;
import com.journalsystem.backend.entity.*;
import com.journalsystem.backend.repository.IMessageRepository;
import com.journalsystem.backend.repository.IUserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MessageService{

    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;

    public MessageService(IMessageRepository messageRepository, IUserRepository userRepository){

        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public MessageResponse sendMessage(String senderEmail, Long receiverId, MessageRequest request){

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Receiver not found"));

        validateContactPermission(sender, receiver);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());

        Message savedMessage = messageRepository.save(message);

        return savedMessage.toMessageResponse();
    }

    public List<MessageResponse> getConversation(String userEmail, Long otherUserId){

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Other user not found"));

        validateContactPermission(user, otherUser);

        return messageRepository.findConversation(
                        user.getId(),
                        otherUser.getId()
                ).stream()
                .map(Message::toMessageResponse)
                .toList();
    }

    public List<UserResponse> getContacts(String userEmail){

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        List<User> contacts = userRepository.findAll()
                .stream()
                .filter(contact -> {
                    if(user instanceof Patient){
                        return contact instanceof Practitioner
                                || contact instanceof Staff;
                    }

                    if(user instanceof Practitioner || user instanceof Staff){
                        return contact instanceof Patient;
                    }

                    return false;
                })
                .toList();

        return contacts.stream()
                .map(User::toUserResponse)
                .toList();
    }

    private void validateContactPermission(User sender, User receiver){

        boolean senderIsPatient = sender instanceof Patient;
        boolean senderIsStaff = sender instanceof Staff;
        boolean senderIsDoctor = sender instanceof Practitioner;

        boolean receiverIsPatient = receiver instanceof Patient;
        boolean receiverIsStaff = receiver instanceof Staff;
        boolean receiverIsDoctor = receiver instanceof Practitioner;

        boolean allowed =
                (senderIsPatient && (receiverIsStaff || receiverIsDoctor))
                        ||
                        ((senderIsStaff || senderIsDoctor) && receiverIsPatient);

        if(!allowed){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to contact this user"
            );
        }
    }
}

