package com.example.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    private String id;
    private String deviceName;
    private String partNumber;
    private String buildingName;
    private String deviceType;
    private Integer NumberOfShelfPositions;


}
