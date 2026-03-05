package com.example.assignment.Exception;

public class ShelfNotFoundException extends RuntimeException{
    public ShelfNotFoundException(String msg){
        super(msg);
    }
}
