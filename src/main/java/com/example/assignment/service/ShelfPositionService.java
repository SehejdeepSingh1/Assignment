package com.example.assignment.service;
import com.example.assignment.Exception.DeviceNotFoundException;
import com.example.assignment.model.Device;
import com.example.assignment.model.ShelfPosition;
import com.example.assignment.repository.ShelfPositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class ShelfPositionService {
    private final ShelfPositionRepository shelfPositionRepository;
    public ShelfPosition createShelfPosition(ShelfPosition shelfPosition){
        String id=UUID.randomUUID().toString();
        shelfPosition.setId(id);
        log.info("Creating ShelfPosition with id: {}",id);
        shelfPositionRepository.createShelfPosition(shelfPosition);
        log.info("ShelfPosition created successfully with id: {}",id);
        return shelfPosition;
    }
    public List<ShelfPosition> getAllShelfPositions(String id){
        log.info("Fetching all ShelfPositions for deviceId: {}",id);
        List<ShelfPosition> list=shelfPositionRepository.getAllShelfPositions(id);
        log.debug("Total ShelfPositions fetched: {}",list.size());
        return list;
    }
    public ShelfPosition getShelfPositionById(String id){
        log.info("Fetching ShelfPosition by id: {}",id);
        ShelfPosition sp=shelfPositionRepository.getShelfPositionById(id);
        if(sp==null){
            log.warn("ShelfPosition not found with id: {}",id);
            throw new DeviceNotFoundException("Shelf Not found with id : "+id);
        }
        log.debug("ShelfPosition found with id: {}",id);
        return shelfPositionRepository.getShelfPositionById(id);
    }
    public void deleteShelfPosition(String id){
        log.info("Deleting ShelfPosition with id: {}",id);
        shelfPositionRepository.getShelfPositionById(id);
        shelfPositionRepository.deleteShelfPosition(id);
        log.info("ShelfPosition deleted successfully with id: {}",id);
    }
    public void addShelfPositions(String id,int numberOfShelfPositions){
        log.info("Adding {} ShelfPositions for deviceId: {}",numberOfShelfPositions,id);
        for(int i=1;i<=numberOfShelfPositions;i++){
            shelfPositionRepository.addShelfPositions(id);
            log.debug("ShelfPosition {} added for deviceId: {}",i,id);
        }
        log.info("All ShelfPositions added for deviceId: {}",id);
    }
}
