package com.example.assignment.model;

public class ShelfPositionResponse {
    private String id;
    private String deviceId;
    private int positiveNumber;
    private boolean isDeleted;
    private boolean isOccupied;


    public ShelfPositionResponse(String id, String deviceId,int positiveNumber,boolean isDeleted,boolean isOccupied) {
        this.id = id;
        this.deviceId=deviceId;
        this.positiveNumber = positiveNumber;
        this.isDeleted=isDeleted;
        this.isOccupied = isOccupied;
    }


}
