package com.example.assignment.controller;

import com.example.assignment.model.Shelf;
import com.example.assignment.service.ShelfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelves")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class ShelfController {

    private final ShelfService shelfService;

    @PostMapping("/create")
    public ResponseEntity<Shelf> createShelf(@RequestBody Shelf shelf){
        log.info("Received request to create shelf: {}", shelf);
        Shelf created=shelfService.createShelf(shelf);
        log.info("Shelf created successfully with id: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/getAllShelves")
    public ResponseEntity<List<Shelf>> getAllShelves(){
        log.info("Received request to fetch all shelves");
        List<Shelf> shelfList = shelfService.getAllShelves();
        log.info("Total shelves fetched: {}", shelfList.size());

        return ResponseEntity.ok(shelfList);
    }
    @GetMapping("/getAvailableShelves")
    public ResponseEntity<List<Shelf>> getAvailableShelves(){
        log.info("Received request to fetch all shelves");
        List<Shelf> shelfList = shelfService.getAvailableShelves();
        log.info("Total shelves fetched: {}", shelfList.size());

        return ResponseEntity.ok(shelfList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shelf> getShelfById(@PathVariable String id){
        log.info("Received request to fetch shelf with id: {}", id);
        Shelf shelf = shelfService.getShelfById(id);
        log.info("Shelf fetched successfully: {}", shelf);
        return ResponseEntity.ok(shelf);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Shelf> getShelfByName(@PathVariable String name){
        log.info("Received request to fetch shelf with name: {}", name);
        Shelf shelf = shelfService.getShelfByName(name);
        log.info("Shelf fetched successfully: {}", shelf);
        return ResponseEntity.ok(shelf);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shelf> updateShelf(@PathVariable String id, @RequestBody Shelf shelf){
        log.info("Received request to update shelf with id: {}", id);
        shelfService.updateShelf(id, shelf);
        log.info("Shelf updated successfully with id: {}", id);
        return ResponseEntity.ok(shelf);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteShelf(@PathVariable String id){
        log.info("Received request to delete shelf with id: {}", id);
        shelfService.deleteShelf(id);
        log.info("Shelf deleted successfully with id: {}", id);
        return ResponseEntity.ok(true);
    }


    @PostMapping("/{shelfid}/assign/{shelfpositionid}")
    public ResponseEntity<String> assignShelf(@PathVariable String shelfid,@PathVariable String shelfpositionid){
        log.info("Received request to assign shelf position {} to shelf {}", shelfpositionid, shelfid);
        shelfService.assignShelf(shelfid, shelfpositionid);
        log.info("Shelf position {} assigned successfully to shelf {}", shelfpositionid, shelfid);
        return ResponseEntity.ok("Shelf assigned successfully");
    }

}
