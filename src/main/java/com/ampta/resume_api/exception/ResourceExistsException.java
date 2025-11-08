package com.ampta.resume_api.exception;

public class ResourceExistsException extends RuntimeException{

    public ResourceExistsException(String message){
        super(message);
    }
}
