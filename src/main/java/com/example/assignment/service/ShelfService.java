package com.example.assignment.service;

import com.example.assignment.Exception.DeviceNotFoundException;
import com.example.assignment.model.Device;
import com.example.assignment.model.Shelf;
import com.example.assignment.repository.DeviceRepository;
import com.example.assignment.repository.ShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShelfService {
    private final ShelfRepository shelfRepository;

    public Shelf createShelf(Shelf shelf){
        try{
            shelf.setId(UUID.randomUUID().toString());
            shelfRepository.createShelf(shelf);
            return shelf;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Shelf> getAllShelves(){
        try{
            return shelfRepository.getAllShelves();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Shelf getShelfById(String id){
        Shelf shelf=shelfRepository.getShelfById(id);
        if(shelf==null){
            throw new DeviceNotFoundException(
                    "Shelf not found with id : "+id
            );
        }
        return shelf;
    }

    public void updateShelf(String id, Shelf updated){
        try{
            Shelf existing=shelfRepository.getShelfById(id);
            existing.setShelfName(updated.getShelfName());
            existing.setPartNumber(updated.getPartNumber());

            shelfRepository.updateShelf(existing);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteShelf(String id){
        try{
            getShelfById(id);
            shelfRepository.deleteShelf(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void assignShelf(String shelfid, String shelfpositionid) {
        getShelfById(shelfid);
        shelfRepository.assignShelftoShelfPosition(shelfid,shelfpositionid);
    }
}
