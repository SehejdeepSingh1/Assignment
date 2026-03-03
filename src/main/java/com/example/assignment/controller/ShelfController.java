package com.example.assignment.controller;

import com.example.assignment.model.Shelf;
import com.example.assignment.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelves")
@RequiredArgsConstructor
@CrossOrigin
public class ShelfController {

    private final ShelfService shelfService;

    @PostMapping("/create")
    public ResponseEntity<Shelf> createShelf(@RequestBody Shelf shelf){
        Shelf created=shelfService.createShelf(shelf);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/getAllShelves")
    public ResponseEntity<List<Shelf>> getAllShelves(){
        List<Shelf> shelfList=shelfService.getAllShelves();
        return ResponseEntity.ok(shelfList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shelf> getShelfById(String id){
        Shelf shelf=shelfService.getShelfById(id);
        return ResponseEntity.ok(shelf);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shelf> updateShelf(@PathVariable String id, @RequestBody Shelf shelf){
        shelfService.updateShelf(id,shelf);
        return ResponseEntity.ok(shelf);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteShelf(@PathVariable String id){
        shelfService.deleteShelf(id);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/{shelfid}/assign/{shelfpositionid}")
    public ResponseEntity<String> assignShelf(@PathVariable String shelfid,@PathVariable String shelfpositionid){
        shelfService.assignShelf(shelfid,shelfpositionid);
        return ResponseEntity.ok("Shelf assigned successfully");
    }

}
