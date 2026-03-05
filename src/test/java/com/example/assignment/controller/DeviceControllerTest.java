package com.example.assignment.controller;

import com.example.assignment.model.Device;

import com.example.assignment.service.DeviceService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)

class DeviceControllerTest {

    @Autowired

    private MockMvc mockMvc;

    @MockBean

    private DeviceService deviceService;

    @Autowired

    private ObjectMapper objectMapper;

    private Device createSampleDevice() {

        Device device = new Device();

        device.setId("123");

        device.setDeviceName("Router");

        device.setPartNumber("PN-123");

        device.setBuildingName("Building-A");

        device.setDeviceType("Network");

        device.setNumberOfShelfPositions(5);

        device.setIsDeleted(false);

        return device;

    }

    @Test

    void testCreateDevice() throws Exception {

        Device device = createSampleDevice();

        when(deviceService.createDevice(device)).thenReturn(device);

        mockMvc.perform(post("/api/devices/create")

                        .contentType("application/json")

                        .content(objectMapper.writeValueAsString(device)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.deviceName").value("Router"));

    }

    @Test

    void testGetAllDevices() throws Exception {

        Device device = createSampleDevice();

        when(deviceService.getAllDevices()).thenReturn(List.of(device));

        mockMvc.perform(get("/api/devices"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.size()").value(1))

                .andExpect(jsonPath("$[0].deviceName").value("Router"));

    }

    @Test

    void testGetDeviceById() throws Exception {

        Device device = createSampleDevice();

        when(deviceService.getDeviceById("123")).thenReturn(device);

        mockMvc.perform(get("/api/devices/123"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value("123"))

                .andExpect(jsonPath("$.deviceName").value("Router"));

    }

    @Test

    void testUpdateDevice() throws Exception {

        Device device = createSampleDevice();

        doNothing().when(deviceService).updateDevice("123", device);

        mockMvc.perform(put("/api/devices/123")

                        .contentType("application/json")

                        .content(objectMapper.writeValueAsString(device)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.deviceName").value("Router"));

    }

    @Test

    void testDeleteDevice() throws Exception {

        doNothing().when(deviceService).deleteDevice("123");

        mockMvc.perform(delete("/api/devices/123"))

                .andExpect(status().isOk())

                .andExpect(content().string("Device deleted successfully"));

    }

}
