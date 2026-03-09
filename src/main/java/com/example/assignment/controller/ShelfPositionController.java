package com.example.assignment.controller;
import com.example.assignment.model.Device;
import com.example.assignment.model.ShelfPosition;
import com.example.assignment.service.ShelfPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/shelf/shelfpositions")
@CrossOrigin
@Slf4j
public class ShelfPositionController {
    private final ShelfPositionService shelfPositionService;
    private final ResourceUrlProvider resourceUrlProvider;
    @PostMapping
    public ResponseEntity<ShelfPosition> createShelfPosition(@RequestBody ShelfPosition shelfPosition){
        log.info("Request received to create ShelfPosition with id: {}", shelfPosition.getId());
        ShelfPosition created=shelfPositionService.createShelfPosition(shelfPosition);
        log.info("ShelfPosition created successfully with id: {}", created.getId());
        return ResponseEntity.ok(created);
    }
    @GetMapping("/device/{deviceid}")
    public ResponseEntity<List<ShelfPosition>> getAllShelfPositions(@PathVariable String deviceid){
        log.info("Fetching all ShelfPositions for deviceId: {}", deviceid);
        List<ShelfPosition> list=shelfPositionService.getAllShelfPositions(deviceid);
        log.debug("Total ShelfPositions fetched: {}", list.size());
        return ResponseEntity.ok(list);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ShelfPosition> getShelfPositionById(@PathVariable String deviceid){
        log.info("Fetching ShelfPosition by id: {}", deviceid);
        ShelfPosition sp=shelfPositionService.getShelfPositionById(deviceid);
        log.debug("ShelfPosition fetched for id: {}", deviceid);
        return ResponseEntity.ok(sp);
    }
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<String> delete(@PathVariable String deviceId){
        log.info("Deleting ShelfPosition with deviceId: {}", deviceId);
        shelfPositionService.deleteShelfPosition(deviceId);
        log.info("ShelfPosition deleted successfully for deviceId: {}", deviceId);
        return ResponseEntity.ok("Shelf Position deleted successfully");
    }

    @PutMapping("/{id}/{numberOfShelfPositions}")
    public ResponseEntity<String> addShelfPositions(@PathVariable String id,@PathVariable int numberOfShelfPositions){
        log.info("Adding {} ShelfPositions to deviceId: {}", numberOfShelfPositions,id);
        shelfPositionService.addShelfPositions(id,numberOfShelfPositions);
        log.info("ShelfPositions added successfully to deviceId: {}", id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/shelfname/{shelfPositionId}")
    public ResponseEntity<String> returnShelfName(@PathVariable String shelfPositionId){
        return ResponseEntity.ok(shelfPositionService.returnShelfName(shelfPositionId));
    }
}
