package com.example.assignment.service;
import com.example.assignment.Exception.DeviceNotFoundException;
import com.example.assignment.model.ShelfPosition;
import com.example.assignment.repository.ShelfPositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class ShelfPositionServiceTest {
    @Mock
    private ShelfPositionRepository shelfPositionRepository;
    @InjectMocks
    private ShelfPositionService shelfPositionService;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    private ShelfPosition createSampleShelfPosition() {
        ShelfPosition sp = new ShelfPosition();
        sp.setId("SP-1");
        sp.setDeviceId("DEV-1");
        sp.setIsDeleted(false);
        sp.setIsOccupied(false);
        return sp;
    }
    @Test
    void testCreateShelfPosition() {
        ShelfPosition sp = createSampleShelfPosition();
        sp.setId(null);
        ShelfPosition result = shelfPositionService.createShelfPosition(sp);
        assertNotNull(result.getId());
        verify(shelfPositionRepository, times(1))
                .createShelfPosition(sp);
    }
    @Test
    void testGetAllShelfPositions() {
        ShelfPosition sp = createSampleShelfPosition();
        when(shelfPositionRepository.getAllShelfPositions("DEV-1"))
                .thenReturn(List.of(sp));
        List<ShelfPosition> list =
                shelfPositionService.getAllShelfPositions("DEV-1");
        assertEquals(1, list.size());
        assertEquals("DEV-1", list.get(0).getDeviceId());
        verify(shelfPositionRepository)
                .getAllShelfPositions("DEV-1");
    }
    @Test
    void testGetShelfPositionByIdSuccess() {
        ShelfPosition sp = createSampleShelfPosition();
        when(shelfPositionRepository.getShelfPositionById("SP-1"))
                .thenReturn(sp);
        ShelfPosition result =
                shelfPositionService.getShelfPositionById("SP-1");
        assertEquals("SP-1", result.getId());
        assertEquals("DEV-1", result.getDeviceId());
    }
    @Test
    void testGetShelfPositionByIdNotFound() {
        when(shelfPositionRepository.getShelfPositionById("SP-1"))
                .thenReturn(null);
        assertThrows(DeviceNotFoundException.class, () ->
                shelfPositionService.getShelfPositionById("SP-1")
        );
    }
    @Test
    void testDeleteShelfPosition() {
        ShelfPosition sp = createSampleShelfPosition();
        when(shelfPositionRepository.getShelfPositionById("SP-1"))
                .thenReturn(sp);
        shelfPositionService.deleteShelfPosition("SP-1");
        verify(shelfPositionRepository)
                .deleteShelfPosition("SP-1");
    }
    @Test
    void testAddShelfPositions() {
        shelfPositionService.addShelfPositions("DEV-1", 3);
        verify(shelfPositionRepository, times(3))
                .addShelfPositions("DEV-1");
    }
}
