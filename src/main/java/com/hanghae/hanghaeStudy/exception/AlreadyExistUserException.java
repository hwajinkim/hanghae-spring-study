package com.hanghae.hanghaeStudy.exception;

public class AlreadyExistUserException extends RuntimeException{
    public AlreadyExistUserException(String message){
        super(message);
    }
}
