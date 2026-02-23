package com.example.assignment.repository;

import com.example.assignment.model.Device;
import com.example.assignment.model.Shelf;
import com.example.assignment.model.ShelfPosition;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.boot.web.server.Ssl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ShelfRepository {

    private final Driver driver;

    public void createShelf(Shelf shelf){
        try(Session session=driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        CREATE (s:Shelf {
                        id:$id,
                        shelfName:$shelfName,
                        partNumber:$partNumber
                        })
                        """, Map.of(
                                "id",shelf.getId(),
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
                        RETURN s
                """);

                List<Shelf> shelves=new ArrayList<>();

                while(result.hasNext()){
                    Record record=result.next();
                    org.neo4j.driver.types.Node node=record.get("s").asNode();

                    shelves.add(new Shelf(
                            node.get("id").asString(),
                            node.get("shelfName").asString(),
                            node.get("partNumber").asString()
                    ));
                }
                return shelves;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Shelf getShelfById(String id){
        try(Session session= driver.session()){
            return session.executeRead(tx -> {
                Result result=tx.run("""
                    MATCH (s:Shelf {id:$id})
                    RETURN s
                """,Map.of(
                        "id",id
                ));

                if(!result.hasNext()){
                    return null;
                }

                Record record=result.next();
                org.neo4j.driver.types.Node node=record.get("s").asNode();

                Shelf shelf=new Shelf(
                        node.get("id").asString(),
                        node.get("shelfName").asString(),
                        node.get("partNumber").asString()
                );
                return shelf;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void updateShelf(Shelf shelf) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                return tx.run("""
                                MATCH (s:Shelf {id:$id})
                                SET s.shelfName=shelfName,
                                s.partNumber=$partNumber
                        """, Map.of(
                        "id", shelf.getId(),
                        "shelfName", shelf.getShelfName(),
                        "partNumber", shelf.getPartNumber()
                ));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ;
    }
    public void deleteShelf(String id){
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                return tx.run("""
                        MATCH (s:Shelf (id:$id)}
                        DETACH DELETE d
                        """,Map.of("id",id));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
