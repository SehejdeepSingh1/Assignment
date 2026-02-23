package com.example.assignment.repository;

import com.example.assignment.model.Device;
import lombok.RequiredArgsConstructor;
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
public class DeviceRepository {
    private final Driver driver;

    public void createDevice(Device device){
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                tx.run("""
                        CREATE (d:Device {
                        id:$id,
                        deviceName:$deviceName,
                        partNumber:$partNumber,
                        buildingName:$buildingName,
                        deviceType:$deviceType,
                        numberOfShelfPositions:$numberOfShelfPositions
                        })
                    """, Map.of(
                        "id",device.getId(),
                        "deviceName",device.getDeviceName(),
                        "partNumber",device.getPartNumber(),
                        "BuildingName",device.getBuildingName(),
                        "deviceType",device.getDeviceType(),
                        "numberOfShelfPositions",device.getNumberOfShelfPositions()
                ));
                return null;
            });
        }catch (Exception e){
            throw new RuntimeException("Error creating device ",e);
        }
    }

    public List<Device> getAllDevices(){
        try(Session session=driver.session()){
            return session.executeRead( tx -> {
                Result result=tx.run("""
                    MATCH (d:Device)
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
                            node.get("BuildingName").asString(),
                            node.get("deviceType").asString(),
                            node.get("numberOfShelfPositions").asInt()
                    ));
                }
                return devices;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Device getDeviceById(String id){
        try(Session session= driver.session()){
            return session.executeRead(tx -> {
                Result result=tx.run("""
                    MATCH (d:Device {id:$id})
                    RETURN d
                """,Map.of("id",id));

                if(!result.hasNext()){
                    return null;
                }

                Record record=result.next();
                org.neo4j.driver.types.Node node=record.get("d").asNode();

                Device device=new Device(
                        node.get("id").asString(),
                        node.get("deviceName").asString(),
                        node.get("partNumber").asString(),
                        node.get("BuildingName").asString(),
                        node.get("deviceType").asString(),
                        node.get("numberOfShelfPositions").asInt()
                );
                return  device;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDevice(Device device){
        try(Session session=driver.session()){
            session.executeWrite(tx -> {
                return tx.run("""
                        MATCH (d:Device {id:$id})
                        SET d.deviceName=$deviceName,
                        d.partNumber=$partNumber,
                        d.BuildingName=$BuildingName,
                        d.deviceType=$deviceType,
                        d.numberOfShelfPositions=$numberOfShelfPositions
                """,Map.of(
                        "id",device.getId(),
                        "deviceName",device.getDeviceName(),
                        "partNumber",device.getPartNumber(),
                        "BuildingName",device.getBuildingName(),
                        "deviceType",device.getDeviceType(),
                        "numberOfShelfPositions",device.getNumberOfShelfPositions()
                ));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        };
    }

    public void deleteDevice(String id){
        try(Session session= driver.session()){
            session.executeWrite(tx -> {
                return tx.run("""
                        MATCH (d:Device (id:$id)}
                        DETACH DELETE d
                        """,Map.of("id",id));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
