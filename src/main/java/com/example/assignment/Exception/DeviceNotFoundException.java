package com.example.assignment.Exception;

public class DeviceNotFoundException extends RuntimeException{

    public DeviceNotFoundException(String msg){
        super(msg);
    }
}
