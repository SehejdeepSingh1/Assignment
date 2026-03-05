package com.example.assignment.service;

import com.example.assignment.Exception.DeviceNotFoundException;
import com.example.assignment.model.Device;
import com.example.assignment.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public Device createDevice(Device device){
        try{
            log.info("Creating new device: {}",device);
            device.setId(UUID.randomUUID().toString());
            deviceRepository.createDevice(device);
            log.info("Device created successfully");
            return device;
        } catch (Exception e) {
            log.error("Error while creating device: {}", device, e);
            throw new RuntimeException(e);
        }
    }

    public List<Device> getAllDevices(){
        try{
            log.info("Fetching all devices from repository");
            List<Device> devices=deviceRepository.getAllDevices();
            log.info("Total devices fetched: {}", devices.size());
            return devices;
        } catch (Exception e) {
            log.error("Error while fetching all devices", e);
            throw new RuntimeException(e);
        }
    }

    public Device getDeviceById(String id){
        log.info("Fetching device with id: {}", id);
        Device device=deviceRepository.getDeviceById(id);
        if(device==null){
            log.warn("Device not found with id: {}", id);
            throw new DeviceNotFoundException(
                    "Device not found with id : "+id
            );
        }
        log.info("Device fetched successfully: {}", device);
        return device;
    }

    public void updateDevice(String id,Device updated){
        try{
            log.info("Updating device with id: {}", id);
            Device existing=deviceRepository.getDeviceById(id);
            if(existing == null){
                log.warn("Attempt to update non-existing device with id: {}", id);
                throw new DeviceNotFoundException("Device not found with id : " + id);
            }
            existing.setDeviceName(updated.getDeviceName());
            existing.setDeviceType(updated.getDeviceType());
            existing.setBuildingName(updated.getBuildingName());
            existing.setPartNumber(updated.getPartNumber());
            existing.setNumberOfShelfPositions(updated.getNumberOfShelfPositions());

            deviceRepository.updateDevice(existing);
            log.info("Device updated successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error while updating device with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }

    public void deleteDevice(String id){
        try{
            log.info("Deleting device with id: {}", id);
            getDeviceById(id);
            deviceRepository.deleteDevice(id);
            log.info("Device deleted successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error while deleting device with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }
}
