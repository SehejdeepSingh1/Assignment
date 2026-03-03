package com.example.assignment.repository;

import com.example.assignment.model.ShelfPosition;
import com.example.assignment.model.ShelfPositionResponse;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ShelfPositionRepository {
    private final Driver driver;

    public void createShelfPosition(ShelfPosition shelfPosition){
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (d:Device {id:$id})
                        CREATE (sp:shelfPosition {
                        id:$id,
                        deviceId:$deviceId,
                        positionNumber:$positionNumber,
                        isDeleted:false,
                        isOccupied:false
                        })
                        CREATE (d)-[:HAS]->(sp)
                        """, Map.of(
                                "id",shelfPosition.getId(),
                                "deviceId",shelfPosition.getDeviceId(),
                                "positionNumber",shelfPosition.getPositionNumber()
                ));
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ShelfPosition> getAllShelfPositions(String deviceId) {
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
                            node.get("positionNumber").asInt(),
                            node.get("isDeleted").asBoolean(),
                            node.get("isOccupied").asBoolean()
                    ));
                }

                return positions;

            });

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }


    public ShelfPosition getShelfPositionById(String deviceId){
        try(Session session= driver.session()){
            return session.executeRead(tx -> {
                Result result=tx.run("""
                        MATCH (sp:ShelfPosition {deviceId:$deviceId})
                        RETURN sp
                        """,Map.of(
                                "deviceId",deviceId
                ));
                if(!result.hasNext()){
                    return null;
                }

                Record record=result.next();
                org.neo4j.driver.types.Node node=record.get("sp").asNode();
                return new ShelfPosition(
                        node.get("id").asString(),
                        node.get("deviceId").asString(),
                        node.get("positionNumber").asInt(),
                        node.get("isDeleted").asBoolean(),
                        node.get("isOccupied").asBoolean()
                );
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateShelfPosition(ShelfPosition shelfPosition){
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (sp:ShelfPosition8 {id:$id})
                        SET sp.positionNumber=$positionNumber
                        """,Map.of(
                                "id",shelfPosition.getId(),
                        "positionNumber",shelfPosition.getPositionNumber()
                ));
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteShelfPosition(String deviceId){
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (sp:ShelfPosition {deviceId:$deviceId})
                        SET sp.isDeleted=true
                        """,Map.of(
                                "deviceId",deviceId
                ));
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
