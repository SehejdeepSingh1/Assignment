package com.example.assignment.controller;

import com.example.assignment.model.ShelfPosition;
import com.example.assignment.service.ShelfPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/shelf/shelfpositions")
@CrossOrigin
public class ShelfPositionController {
    private final ShelfPositionService shelfPositionService;
    private final ResourceUrlProvider resourceUrlProvider;

    @PostMapping
    public ResponseEntity<ShelfPosition> createShelfPosition(
            @RequestBody ShelfPosition shelfPosition
    ){
        return  ResponseEntity.ok(shelfPositionService.createShelfPosition(shelfPosition));
    }

    @GetMapping("/device/{deviceid}")
    public ResponseEntity<List<ShelfPosition>> getAllShelfPositions(@PathVariable String deviceid){
        return ResponseEntity.ok(shelfPositionService.getAllShelfPositions(deviceid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfPosition> getShelfPositionById(@PathVariable String deviceid){
        return ResponseEntity.ok(shelfPositionService.getShelfPositionById(deviceid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable String id,
            @RequestBody ShelfPosition shelfPosition
    ){
        shelfPositionService.updateShelfPosition(id,shelfPosition);
        return ResponseEntity.ok("Shelf Position is updated successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(String deviceId){
        shelfPositionService.deleteShelfPosition(deviceId);
        return ResponseEntity.ok("Shelf Position deleted successfully");
    }
}
