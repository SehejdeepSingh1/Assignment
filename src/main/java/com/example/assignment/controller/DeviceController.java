package com.example.assignment.controller;

import com.example.assignment.model.Device;
import com.example.assignment.service.DeviceService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/create")
    public ResponseEntity<Device> createDevice(@RequestBody Device device){
        log.info("Request received to create device: {}", device);
        Device createdDevice=deviceService.createDevice(device);
        System.out.println(device);
        log.info("Device created successfully with id: {}", createdDevice.getId());
        return ResponseEntity.ok(createdDevice);
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices(){
        log.info("Request received to fetch all devices");
        List<Device> deviceList=deviceService.getAllDevices();
        log.info("Fetched {} devices from database", deviceList.size());
        return ResponseEntity.ok(deviceList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String id){
        log.info("Request received to fetch device with id: {}", id);
        Device device=deviceService.getDeviceById(id);
        log.info("Device fetched successfully: {}", device);
        return ResponseEntity.ok(device);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable String id,@RequestBody Device device){
        log.info("Request received to update device with id: {}", id);
        deviceService.updateDevice(id,device);
        log.info("Device updated successfully: {}", device);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDevice(@PathVariable String id){
        log.info("Request received to delete device with id: {}", id);
        deviceService.deleteDevice(id);
        log.info("Device deleted successfully with id: {}", id);
        return ResponseEntity.ok("Device deleted successfully");
    }
}
