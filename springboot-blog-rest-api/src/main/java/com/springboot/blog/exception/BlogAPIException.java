package com.springboot.blog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException{

    @Getter
    private HttpStatus status;
    private String message;

    public BlogAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
