package com.example.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfPosition {
    private String id;
    private String deviceId;
    private Integer positionNumber;
    private Boolean isDeleted=false;
    private Boolean isOccupied=false;
}
