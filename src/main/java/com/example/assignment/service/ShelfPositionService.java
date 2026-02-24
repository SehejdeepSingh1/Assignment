package com.example.assignment.service;

import com.example.assignment.Exception.DeviceNotFoundException;
import com.example.assignment.model.Device;
import com.example.assignment.model.ShelfPosition;
import com.example.assignment.repository.ShelfPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShelfPositionService {
    private final ShelfPositionRepository shelfPositionRepository;

    public ShelfPosition createShelfPosition(ShelfPosition shelfPosition){
        shelfPosition.setId(UUID.randomUUID().toString());
        shelfPositionRepository.createShelfPosition(shelfPosition);
        return shelfPosition;
    }

    public List<ShelfPosition> getAllShelfPositions(String id){
        return shelfPositionRepository.getAllShelfPositions(id);
    }

    public ShelfPosition getShelfPositionById(String id){
        ShelfPosition sp=shelfPositionRepository.getShelfPositionById(id);

        if(sp==null){
            throw new DeviceNotFoundException("Shelf Not found with id : "+id);
        }
        return shelfPositionRepository.getShelfPositionById(id);
    }

    public void updateShelfPosition(String id,ShelfPosition updated){
        ShelfPosition existing=shelfPositionRepository.getShelfPositionById(id);
        existing.setPositionNumber(updated.getPositionNumber());
        shelfPositionRepository.updateShelfPosition(updated);
    }

    public void deleteShelfPosition(String id){
        shelfPositionRepository.getShelfPositionById(id);
        shelfPositionRepository.deleteShelfPosition(id);
    }
}
