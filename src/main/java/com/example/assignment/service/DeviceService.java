package com.example.assignment.service;

import com.example.assignment.Exception.DeviceNotFoundException;
import com.example.assignment.model.Device;
import com.example.assignment.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public Device createDevice(Device device){
        try{
            device.setId(UUID.randomUUID().toString());
            deviceRepository.createDevice(device);
            return device;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Device> getAllDevices(){
        try{
            return deviceRepository.getAllDevices();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Device getDeviceById(String id){
        Device device=deviceRepository.getDeviceById(id);
        if(device==null){
            throw new DeviceNotFoundException(
                    "Device not found with id : "+id
            );
        }
        return device;
    }

    public void updateDevice(String id,Device updated){
        try{
            Device existing=deviceRepository.getDeviceById(id);
            existing.setDeviceName(updated.getDeviceName());
            existing.setDeviceType(updated.getDeviceType());
            existing.setBuildingName(updated.getBuildingName());
            existing.setPartNumber(updated.getPartNumber());
            existing.setNumberOfShelfPositions(updated.getNumberOfShelfPositions());

            deviceRepository.updateDevice(existing);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDevice(String id){
        try{
            getDeviceById(id);
            deviceRepository.deleteDevice(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
