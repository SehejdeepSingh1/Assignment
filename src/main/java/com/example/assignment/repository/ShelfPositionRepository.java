package com.example.assignment.repository;
import com.example.assignment.model.ShelfPosition;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.neo4j.driver.Driver;

import org.neo4j.driver.Session;

import org.neo4j.driver.Result;

import org.neo4j.driver.Record;

import org.neo4j.driver.types.Node;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;

import java.util.List;

import java.util.Map;

import java.util.UUID;
@Repository
@RequiredArgsConstructor
@Slf4j
public class ShelfPositionRepository {
    private final Driver driver;
    public void createShelfPosition(ShelfPosition shelfPosition){
        log.info("Creating ShelfPosition with id: {} for deviceId: {}",shelfPosition.getId(),shelfPosition.getDeviceId());
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (d:Device {id:$id})
                        CREATE (sp:shelfPosition {
                        id:$id,
                        deviceId:$deviceId,
                        isDeleted:false,
                        isOccupied:false
                        })
                        CREATE (d)-[:HAS]->(sp)
                        """, Map.of(
                        "id",shelfPosition.getId(),
                        "deviceId",shelfPosition.getDeviceId()
                ));
                log.debug("ShelfPosition node created in Neo4j with id: {}",shelfPosition.getId());
                return null;
            });
        } catch (Exception e) {
            log.error("Error creating ShelfPosition for deviceId: {}",shelfPosition.getDeviceId(),e);
            throw new RuntimeException(e);
        }
    }
    public List<ShelfPosition> getAllShelfPositions(String deviceId) {
        log.info("Fetching all ShelfPositions for deviceId: {}",deviceId);
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                    MATCH (d:Device {id: $deviceId})-[:HAS]->(sp:ShelfPosition)
                    WHERE sp.isDeleted = false
                    OPTIONAL MATCH (sp)-[:ASSIGNED_TO]->(s:Shelf)
                    RETURN sp,
                           CASE WHEN s IS NOT NULL THEN true ELSE false END AS isOccupied
                    """,
                        Map.of("deviceId", deviceId)
                );
                List<ShelfPosition> positions = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    Node node = record.get("sp").asNode();
                    boolean isOccupied = record.get("isOccupied").asBoolean();
                    positions.add(new ShelfPosition(
                            node.get("id").asString(),
                            node.get("deviceId").asString(),
                            node.get("isDeleted").asBoolean(),
                            node.get("isOccupied").asBoolean()
                    ));
                }
                log.debug("Total ShelfPositions fetched for deviceId {}: {}",deviceId,positions.size());
                return positions;
            });
        } catch (Exception e) {
            log.error("Error fetching ShelfPositions for deviceId: {}",deviceId,e);
            throw new RuntimeException(e);
        }
    }
    public ShelfPosition getShelfPositionById(String deviceId){
        log.info("Fetching ShelfPosition by deviceId: {}",deviceId);
        try(Session session= driver.session()){
            return session.executeRead(tx -> {
                Result result=tx.run("""
                        MATCH (sp:ShelfPosition {deviceId:$deviceId})
                        RETURN sp
                        """,Map.of(
                        "deviceId",deviceId
                ));
                if(!result.hasNext()){
                    log.warn("ShelfPosition not found for deviceId: {}",deviceId);
                    return null;
                }
                Record record=result.next();
                org.neo4j.driver.types.Node node=record.get("sp").asNode();
                log.debug("ShelfPosition found for deviceId: {}",deviceId);
                return new ShelfPosition(
                        node.get("id").asString(),
                        node.get("deviceId").asString(),
                        node.get("isDeleted").asBoolean(),
                        node.get("isOccupied").asBoolean()
                );
            });
        } catch (Exception e) {
            log.error("Error fetching ShelfPosition for deviceId: {}",deviceId,e);
            throw new RuntimeException(e);
        }
    }
    public void deleteShelfPosition(String id){
        log.info("Deleting ShelfPosition with id: {}",id);
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (sp:ShelfPosition {id:$id})<-[:HAS]-(d:Device)
                        SET sp.isDeleted=true
                        SET d.numberOfShelfPositions=d.numberOfShelfPositions-1
                        """,Map.of(
                        "id",id
                ));
                log.debug("ShelfPosition marked deleted with id: {}",id);
                return null;
            });
        } catch (Exception e) {
            log.error("Error deleting ShelfPosition with id: {}",id,e);
            throw new RuntimeException(e);
        }
    }
    public void addShelfPositions(String deviceId){
        String id=UUID.randomUUID().toString();
        log.info("Adding new ShelfPosition {} for deviceId: {}",id,deviceId);
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (d:Device {id:$deviceId})
                        SET d.numberOfShelfPositions=d.numberOfShelfPositions+1
                        CREATE (sp:ShelfPosition{
                            deviceId:$deviceId,
                            isDeleted:false,
                            isOccupied:false,
                            id:$id
                        })
                        CREATE (d)-[:HAS]->(sp)
                        RETURN d""", Map.of(
                        "deviceId",deviceId,
                        "id",id
                ));
                log.debug("ShelfPosition created and linked to deviceId: {}",deviceId);
                return null;
            });
        } catch (Exception e) {
            log.error("Error adding ShelfPosition for deviceId: {}",deviceId,e);
            throw new RuntimeException(e);
        }
    }
}
