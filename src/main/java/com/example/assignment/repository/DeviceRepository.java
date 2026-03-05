package com.example.assignment.repository;

import com.example.assignment.model.Device;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.cfg.MapperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
@Slf4j
public class DeviceRepository {
    private final Driver driver;

    public void createDevice(Device device){
        log.info("Creating device in Neo4j with id: {}", device.getId());
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                log.debug("Executing CREATE query for device: {}", device.getId());
                tx.run("""
                        CREATE (d:Device {
                        id:$id,
                        deviceName:$deviceName,
                        partNumber:$partNumber,
                        buildingName:$buildingName,
                        deviceType:$deviceType,
                        numberOfShelfPositions:$numberOfShelfPositions,
                        isDeleted:false
                        })
                        
                        WITH d
                        UNWIND range(1,$numberOfShelfPositions) AS i
                        CREATE (sp:ShelfPosition {
                        id:randomUUID(),
                        deviceId:$id,
                        isDeleted:false,
                        isOccupied:false
                        })
                        
                        CREATE (d)-[:HAS]->(sp)
                    """, Map.of(
                        "id",device.getId(),
                        "deviceName",device.getDeviceName(),
                        "partNumber",device.getPartNumber(),
                        "buildingName",device.getBuildingName(),
                        "deviceType",device.getDeviceType(),
                        "numberOfShelfPositions",device.getNumberOfShelfPositions()
                ));
                log.info("Device created successfully in database: {}", device.getId());
                return null;
            });
        }catch (Exception e){
            log.error("Error while creating device in database: {}", device.getId(), e);
            throw new RuntimeException("Error creating device ",e);
        }
    }

    public List<Device> getAllDevices(){
        log.info("Fetching all devices from database");

        try(Session session=driver.session()){
            return session.executeRead( tx -> {
                Result result=tx.run("""
                    MATCH (d:Device)
                    WHERE d.isDeleted=false
                    RETURN d
                """);

                List<Device> devices=new ArrayList<>();

                while(result.hasNext()){
                    Record record=result.next();
                    org.neo4j.driver.types.Node node=record.get("d").asNode();

                    devices.add(new Device(
                            node.get("id").asString(),
                            node.get("deviceName").asString(),
                            node.get("partNumber").asString(),
                            node.get("buildingName").asString(),
                            node.get("deviceType").asString(),
                            node.get("numberOfShelfPositions").asInt(),
                            node.get("isDeleted").asBoolean()
                    ));
                }
                log.info("Total devices fetched from DB: {}", devices.size());
                return devices;
            });
        } catch (Exception e) {
            log.error("Error while fetching all devices from DB", e);
            throw new RuntimeException(e);
        }
    }

    public Device getDeviceById(String id){
        log.info("Fetching device from DB with id: {}", id);
        try(Session session= driver.session()){
            return session.executeRead(tx -> {
                Result result=tx.run("""
                    MATCH (d:Device {id:$id})
                    WHERE d.isDeleted=false
                    RETURN d
                """,Map.of("id",id));

                if(!result.hasNext()){
                    log.warn("Device not found in DB with id: {}", id);
                    return null;
                }

                Record record=result.next();
                org.neo4j.driver.types.Node node=record.get("d").asNode();

                Device device=new Device(
                        node.get("id").asString(),
                        node.get("deviceName").asString(),
                        node.get("partNumber").asString(),
                        node.get("buildingName").asString(),
                        node.get("deviceType").asString(),
                        node.get("numberOfShelfPositions").asInt(),
                        node.get("isDeleted").asBoolean()
                );
                log.info("Device fetched successfully from DB with id: {}", id);
                return  device;
            });
        } catch (Exception e) {
            log.error("Error while fetching device with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }

    public void updateDevice(Device device){
        log.info("Updating device in DB with id: {}", device.getId());
        try(Session session=driver.session()){
            session.executeWrite(tx -> {
                log.debug("Executing UPDATE query for device: {}", device.getId());
                tx.run("""
                        MATCH (d:Device {id:$id})
                        SET d.deviceName=$deviceName,
                        d.partNumber=$partNumber,
                        d.buildingName=$buildingName,
                        d.deviceType=$deviceType,
                        d.numberOfShelfPositions=$numberOfShelfPositions
                """,Map.of(
                        "id",device.getId(),
                        "deviceName",device.getDeviceName(),
                        "partNumber",device.getPartNumber(),
                        "buildingName",device.getBuildingName(),
                        "deviceType",device.getDeviceType(),
                        "numberOfShelfPositions",device.getNumberOfShelfPositions()

                ));
                log.info("Device updated successfully in DB with id: {}", device.getId());
                return null;
            });
        } catch (Exception e) {
            log.error("Error while updating device with id: {}", device.getId(), e);
            throw new RuntimeException(e);
        };
    }

    public void deleteDevice(String id){
        log.info("Soft deleting device with id: {}", id);
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                log.debug("Marking device as deleted for id: {}", id);
                tx.run("""
                        MATCH (d:Device {id:$id})
                        SET d.isDeleted=true
                        """,Map.of("id",id));
                tx.run("""
                        MATCH (d:Device {id:$id})-[:HAS]->(sp:ShelfPosition)
                        SET sp.isDeleted=true;
                        """,Map.of("id",id));
                tx.run("""
                        MATCH (sp:ShelfPosition)-[r:HAS]->(s:Shelf)
                        WHERE sp.deviceId=$id
                        DELETE r
                        """,Map.of("id",id));
                log.info("Device deleted successfully with id: {}", id);
                return null;
            });
        } catch (Exception e) {
            log.error("Error while deleting device with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }



}
