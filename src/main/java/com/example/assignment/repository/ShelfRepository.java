package com.example.assignment.repository;

import com.example.assignment.model.Shelf;
import com.example.assignment.model.ShelfPosition;
import lombok.RequiredArgsConstructor;
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
public class ShelfRepository {
    private final Driver driver;

    public void createShelf(Shelf shelf){
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        CREATE (s:Shelf {
                        id:$id,
                        shelfName:$shelfName,
                        partNumber:$partNumber,
                        isDeleted:false})
                        """, Map.of(
                            "id", shelf.getId(),
                        "shelfName",shelf.getShelfName(),
                        "partNumber",shelf.getPartNumber()
                ));
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Shelf> getAllShelves(){
        try(Session session=driver.session()){
            return session.executeRead(tx -> {
                Result result=tx.run("""
                        MATCH (s:Shelf)
                        WHERE s.isDeleted=false
                        RETURN s
                        """,Map.of());

                List<Shelf> shelves=new ArrayList<>();

                while(result.hasNext()){
                    Record record=result.next();
                    org.neo4j.driver.types.Node node=record.get("s").asNode();

                    shelves.add(new Shelf(
                            node.get("id").asString(),
                            node.get("shelfName").asString(),
                            node.get("partNumber").asString(),
                            node.get("isDeleted").asBoolean()
                    ));
                }
                return shelves;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Shelf getShelfById(String id){
        try(Session session=driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run("""
                        MATCH (s:Shelf {id:$id})
                        RETURN s
                        """, Map.of(
                        "id", id
                ));

                if (!result.hasNext()) {
                    return null;
                }
                Record record = result.next();
                org.neo4j.driver.types.Node node = record.get("s").asNode();
                return new Shelf(
                        node.get("id").asString(),
                        node.get("shelfName").asString(),
                        node.get("partNumber").asString(),
                        node.get("isDeleted").asBoolean()
                );
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateShelf(Shelf updated){
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (s:Shelf {id:$id})
                        SET s.shelfName=$shelfName,
                        s.partNumber=updated.partNumber
                        """,Map.of(
                                "id",updated.getId(),
                        "shelfName",updated.getShelfName(),
                        "partNumber",updated.getPartNumber()

                ));
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteShelf(String id){
        try(Session session= driver.session()){
            session.executeWrite(tx ->{
                //remove relationship
                tx.run("""
                        MATCH (sp:ShelfPosition)-[r:HAS]->(s:Shelf {id:$id})
                        DELETE r
                        """,Map.of(
                                "id",id
                ));
                //shelf mark deleted
                tx.run("""
                        MATCH (s:Shelf {id:$id})
                        SET s.isDeleted=true;
                        """,Map.of("id",id));
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void assignShelftoShelfPosition(String shelfid, String shelfPositionid){
        try(Session session= driver.session()){
            session.executeWrite(tx->{
                Result spcheck=tx.run("""
                    MATCH (sp:ShelfPosition {shelfPositionid:$shelfPositionid})-[:HAS]->(s:Shelf)
                    RETURN s
                """,Map.of("shelfPositionid",shelfPositionid));

                if(spcheck.hasNext()) {
                    throw new RuntimeException("ShelfPosition already occupied");
                }

                Result shelfcheck=tx.run("""
                        MATCH (sp:ShelfPosition)-[:HAS]->(s:Shelf {id:$id})
                        RETURN sp
                        """,Map.of("id",shelfid));
                if(shelfcheck.hasNext()){
                    throw new RuntimeException("Shelf already assigned to Shelf Position");
                }
                tx.run("""
                        MATCH (s:Shelf {id:$id}),
                        (sp:ShelfPosition {id:$spid})
                        CREATE (sp)-[:HAS]->(s)
                        """,Map.of(
                        "id",shelfid,
                        "spid",shelfPositionid
                ));
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
