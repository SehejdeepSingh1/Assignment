package com.example.assignment.controller;

import com.example.assignment.model.Device;

import com.example.assignment.service.DeviceService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)

public class DeviceControllerTest {

    @Autowired

    private MockMvc mockMvc;

    @MockBean

    private DeviceService deviceService;

    @Autowired

    private ObjectMapper objectMapper;

    private Device device;

    @BeforeEach

    void setup(){

        device = new Device();

        device.setId("1");

        device.setName("Test Device");

    }

    // CREATE DEVICE

    @Test

    void testCreateDevice() throws Exception {

        when(deviceService.createDevice(Mockito.any(Device.class))).thenReturn(device);

        mockMvc.perform(post("/api/devices/create")

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(device)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value("1"))

                .andExpect(jsonPath("$.name").value("Test Device"));

    }

    // GET ALL DEVICES

    @Test

    void testGetAllDevices() throws Exception {

        when(deviceService.getAllDevices()).thenReturn(List.of(device));

        mockMvc.perform(get("/api/devices"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].id").value("1"))

                .andExpect(jsonPath("$[0].name").value("Test Device"));

    }

    // GET DEVICE BY ID

    @Test

    void testGetDeviceById() throws Exception {

        when(deviceService.getDeviceById("1")).thenReturn(device);

        mockMvc.perform(get("/api/devices/1"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value("1"))

                .andExpect(jsonPath("$.name").value("Test Device"));

    }

    // UPDATE DEVICE

    @Test

    void testUpdateDevice() throws Exception {

        Mockito.doNothing().when(deviceService).updateDevice(Mockito.eq("1"), Mockito.any(Device.class));

        mockMvc.perform(put("/api/devices/1")

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(device)))

                .andExpect(status().isOk());

    }

    // DELETE DEVICE

    @Test

    void testDeleteDevice() throws Exception {

        Mockito.doNothing().when(deviceService).deleteDevice("1");

        mockMvc.perform(delete("/api/devices/1"))

                .andExpect(status().isOk())

                .andExpect(content().string("Device deleted successfully"));

    }

}
