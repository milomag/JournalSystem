package com.journalsystem.backend.exception;

public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(){
        super("Incorrect email or password.");
    }
}