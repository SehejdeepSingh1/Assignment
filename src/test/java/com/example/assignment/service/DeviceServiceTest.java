package com.example.assignment.service;
import com.example.assignment.Exception.DeviceNotFoundException;
import com.example.assignment.model.Device;
import com.example.assignment.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class DeviceServiceTest {
    @Mock
    private DeviceRepository deviceRepository;
    @InjectMocks
    private DeviceService deviceService;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
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
    void testCreateDevice() {
        Device device = createSampleDevice();
        device.setId(null); // id will be generated inside service
        Device result = deviceService.createDevice(device);
        assertNotNull(result.getId());
        verify(deviceRepository, times(1)).createDevice(device);
    }
    @Test
    void testGetAllDevices() {
        Device device = createSampleDevice();
        when(deviceRepository.getAllDevices())
                .thenReturn(List.of(device));
        List<Device> devices = deviceService.getAllDevices();
        assertEquals(1, devices.size());
        assertEquals("Router", devices.get(0).getDeviceName());
        verify(deviceRepository, times(1)).getAllDevices();
    }
    @Test
    void testGetDeviceByIdSuccess() {
        Device device = createSampleDevice();
        when(deviceRepository.getDeviceById("123"))
                .thenReturn(device);
        Device result = deviceService.getDeviceById("123");
        assertEquals("123", result.getId());
        assertEquals("Router", result.getDeviceName());
    }
    @Test
    void testGetDeviceByIdNotFound() {
        when(deviceRepository.getDeviceById("123"))
                .thenReturn(null);
        assertThrows(DeviceNotFoundException.class, () ->
                deviceService.getDeviceById("123")
        );
    }
    @Test
    void testUpdateDeviceSuccess() {
        Device existing = createSampleDevice();
        Device updated = new Device();
        updated.setDeviceName("UpdatedRouter");
        updated.setPartNumber("PN-999");
        updated.setBuildingName("Building-B");
        updated.setDeviceType("Storage");
        updated.setNumberOfShelfPositions(10);
        when(deviceRepository.getDeviceById("123"))
                .thenReturn(existing);
        deviceService.updateDevice("123", updated);
        assertEquals("UpdatedRouter", existing.getDeviceName());
        assertEquals("PN-999", existing.getPartNumber());
        assertEquals("Building-B", existing.getBuildingName());
        assertEquals("Storage", existing.getDeviceType());
        assertEquals(10, existing.getNumberOfShelfPositions());
        verify(deviceRepository, times(1)).updateDevice(existing);
    }

    @Test
    void testDeleteDeviceSuccess() {
        Device device = createSampleDevice();
        when(deviceRepository.getDeviceById("123"))
                .thenReturn(device);
        deviceService.deleteDevice("123");
        verify(deviceRepository, times(1)).deleteDevice("123");
    }

}
