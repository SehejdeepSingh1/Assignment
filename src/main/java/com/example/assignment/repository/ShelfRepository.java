package com.example.assignment.repository;

import com.example.assignment.model.Shelf;
import com.example.assignment.model.ShelfPosition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ShelfRepository {
    private final Driver driver;
    public void createShelf(Shelf shelf) {
        log.info("Creating shelf with id: {}", shelf.getId());
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("""
                        CREATE (s:Shelf {
                        id:$id,
                        shelfName:$shelfName,
                        partNumber:$partNumber,
                        isDeleted:false,
                        isOccupied:false})
                        """, Map.of(
                        "id", shelf.getId(),
                        "shelfName", shelf.getShelfName(),
                        "partNumber", shelf.getPartNumber()
                ));
                log.debug("Shelf created successfully with id: {}", shelf.getId());
                return null;
            });
        } catch (Exception e) {
            log.error("Error creating shelf with id: {}", shelf.getId(), e);
            throw new RuntimeException(e);
        }
    }
    public List<Shelf> getAllShelves() {
        log.info("Fetching all shelves");
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                        MATCH (s:Shelf)
                        WHERE s.isDeleted=false
                        RETURN s
                        """, Map.of());
                List<Shelf> shelves = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    org.neo4j.driver.types.Node node = record.get("s").asNode();
                    shelves.add(new Shelf(
                            node.get("id").asString(),
                            node.get("shelfName").asString(),
                            node.get("partNumber").asString(),
                            node.get("isDeleted").asBoolean(false),
                            node.get("isOccupied").asBoolean(false)
                    ));
                }
                log.debug("Total shelves fetched: {}", shelves.size());
                return shelves;
            });
        } catch (Exception e) {
            log.error("Error fetching shelves", e);
            throw new RuntimeException(e);
        }
    }
    public Shelf getShelfById(String id) {
        log.info("Fetching shelf with id: {}", id);
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                        MATCH (s:Shelf {id:$id})
                        RETURN s
                        """, Map.of("id", id));
                if (!result.hasNext()) {
                    log.warn("Shelf not found with id: {}", id);
                    return null;
                }
                Record record = result.next();
                org.neo4j.driver.types.Node node = record.get("s").asNode();
                log.debug("Shelf found with id: {}", id);
                return new Shelf(
                        node.get("id").asString(),
                        node.get("shelfName").asString(),
                        node.get("partNumber").asString(),
                        node.get("isDeleted").asBoolean(false),
                        node.get("isOccupied").asBoolean(false)
                );
            });
        } catch (Exception e) {
            log.error("Error fetching shelf with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }

    public void updateShelf(Shelf updated) {
        log.info("Updating shelf with id: {}", updated.getId());
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (s:Shelf {id:$id})
                        SET s.shelfName=$shelfName,
                            s.partNumber=$partNumber
                        """, Map.of(
                        "id", updated.getId(),
                        "shelfName", updated.getShelfName(),
                        "partNumber", updated.getPartNumber()
                ));
                log.debug("Shelf updated successfully with id: {}", updated.getId());
                return null;
            });
        } catch (Exception e) {
            log.error("Error updating shelf with id: {}", updated.getId(), e);
            throw new RuntimeException(e);
        }
    }
    public void deleteShelf(String id) {
        log.info("Deleting shelf with id: {}", id);
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (sp:ShelfPosition)-[r:HAS]->(s:Shelf {id:$id})
                        DELETE r
                        """, Map.of("id", id));
                tx.run("""
                        MATCH (s:Shelf {id:$id})
                        SET s.isDeleted=true
                        """, Map.of("id", id));
                log.debug("Shelf marked as deleted with id: {}", id);
                return null;
            });
        } catch (Exception e) {
            log.error("Error deleting shelf with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }

    public void assignShelftoShelfPosition(String shelfid, String shelfPositionid) {
        log.info("Assigning shelf {} to shelfPosition {}", shelfid, shelfPositionid);
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                Result spcheck = tx.run("""
                            MATCH (sp:ShelfPosition {shelfPositionid:$shelfPositionid})-[:HAS]->(s:Shelf)
                            RETURN s
                        """, Map.of("shelfPositionid", shelfPositionid));
                if (spcheck.hasNext()) {
                    log.warn("ShelfPosition {} already occupied", shelfPositionid);
                    throw new RuntimeException("ShelfPosition already occupied");
                }
                Result shelfcheck = tx.run("""
                        MATCH (sp:ShelfPosition)-[:HAS]->(s:Shelf {id:$id})
                        RETURN sp
                        """, Map.of("id", shelfid));
                if (shelfcheck.hasNext()) {
                    log.warn("Shelf {} already assigned to another ShelfPosition", shelfid);
                    throw new RuntimeException("Shelf already assigned to Shelf Position");
                }
                tx.run("""
                        MATCH (s:Shelf {id:$id}),
                              (sp:ShelfPosition {id:$spid})
                        SET sp.isOccupied=true
                        CREATE (sp)-[:HAS]->(s)
                        """, Map.of(
                        "id", shelfid,
                        "spid", shelfPositionid
                ));
                log.debug("Shelf {} successfully assigned to ShelfPosition {}", shelfid, shelfPositionid);
                return null;
            });
        } catch (Exception e) {
            log.error("Error assigning shelf {} to shelfPosition {}", shelfid, shelfPositionid, e);
            throw new RuntimeException(e);
        }
    }

}
