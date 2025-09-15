package com.lesson.lessons.Exceptions;
//this is a custom exception
public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
}
