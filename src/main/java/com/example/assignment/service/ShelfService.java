package com.example.assignment.service;

import com.example.assignment.Exception.DeviceNotFoundException;
import com.example.assignment.Exception.ShelfNotFoundException;
import com.example.assignment.model.Device;
import com.example.assignment.model.Shelf;
import com.example.assignment.repository.DeviceRepository;
import com.example.assignment.repository.ShelfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelfService {
    private final ShelfRepository shelfRepository;

    public Shelf createShelf(Shelf shelf){
        try{
            log.info("Creating new shelf: {}", shelf);
            shelf.setId(UUID.randomUUID().toString());
            shelfRepository.createShelf(shelf);
            log.info("Shelf created successfully with id: {}", shelf.getId());

            return shelf;
        } catch (Exception e) {
            log.error("Error while creating shelf: {}", shelf, e);
            throw new RuntimeException(e);
        }
    }

    public List<Shelf> getAllShelves(){
        try{
            log.info("Fetching all shelves");
            List<Shelf> shelves = shelfRepository.getAllShelves();
            log.info("Total shelves fetched: {}", shelves.size());
            return shelves;
        } catch (Exception e) {
            log.error("Error while fetching shelves", e);
            throw new RuntimeException(e);
        }
    }
    public List<Shelf> getAvailableShelves(){
        try{
            log.info("Fetching all shelves");
            List<Shelf> shelves = shelfRepository.getAvailableShelves();
            log.info("Total shelves fetched: {}", shelves.size());
            return shelves;
        } catch (Exception e) {
            log.error("Error while fetching shelves", e);
            throw new RuntimeException(e);
        }
    }

    public Shelf getShelfById(String id){
        log.info("Fetching shelf with id: {}", id);
        Shelf shelf=shelfRepository.getShelfById(id);
        if(shelf==null){
            log.warn("Shelf not found with id: {}", id);
            throw new ShelfNotFoundException(
                    "Shelf not found with id : "+id
            );
        }
        log.info("Shelf fetched successfully: {}", shelf);
        return shelf;
    }
    public Shelf getShelfByName(String name){
        log.info("Fetching shelf with name: {}", name);
        Shelf shelf=shelfRepository.getShelfByName(name);
        if(shelf==null){
            log.warn("Shelf not found with name: {}", name);
            throw new ShelfNotFoundException(
                    "Shelf not found with name : "+name
            );
        }
        log.info("Shelf fetched successfully: {}", shelf);
        return shelf;
    }

    public void updateShelf(String id, Shelf updated){
        try{
            log.info("Updating shelf with id: {}", id);
            Shelf existing=shelfRepository.getShelfById(id);
            if(existing == null){
                log.warn("Attempt to update non-existing shelf with id: {}", id);
                throw new DeviceNotFoundException("Shelf not found with id : " + id);
            }

            existing.setShelfName(updated.getShelfName());
            existing.setPartNumber(updated.getPartNumber());

            shelfRepository.updateShelf(existing);
            log.info("Shelf updated successfully with id: {}", id);

        } catch (Exception e) {
            log.error("Error while updating shelf with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }
    public void deleteShelf(String id){
        try{
            log.info("Deleting shelf with id: {}", id);
            getShelfById(id);
            shelfRepository.deleteShelf(id);
            log.info("Shelf deleted successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error while deleting shelf with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }


    public void assignShelf(String shelfid, String shelfpositionid) {
        log.info("Assigning shelf position {} to shelf {}", shelfpositionid, shelfid);
        getShelfById(shelfid);
        shelfRepository.assignShelftoShelfPosition(shelfid, shelfpositionid);
        log.info("Shelf position {} assigned successfully to shelf {}", shelfpositionid, shelfid);
    }
}
