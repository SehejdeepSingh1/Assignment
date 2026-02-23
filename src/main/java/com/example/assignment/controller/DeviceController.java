package com.example.assignment.controller;

import com.example.assignment.model.Device;
import com.example.assignment.service.DeviceService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@CrossOrigin
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device){
        Device createdDevice=deviceService.createDevice(device);
        return ResponseEntity.ok(createdDevice);
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices(){
        List<Device> deviceList=deviceService.getAllDevices();
        return ResponseEntity.ok(deviceList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String id){
        Device device=deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDevice(@PathVariable String id,@RequestBody Device device){
        deviceService.updateDevice(id,device);
        return ResponseEntity.ok("Device updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDevice(@PathVariable String id){
        deviceService.deleteDevice(id);
        return ResponseEntity.ok("Device deleted successfully");
    }
}
