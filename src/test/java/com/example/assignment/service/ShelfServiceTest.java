package com.example.assignment.service;
import com.example.assignment.Exception.ShelfNotFoundException;
import com.example.assignment.model.Shelf;
import com.example.assignment.repository.ShelfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class ShelfServiceTest {
    @Mock
    private ShelfRepository shelfRepository;
    @InjectMocks
    private ShelfService shelfService;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    private Shelf createSampleShelf() {
        Shelf shelf = new Shelf();
        shelf.setId("101");
        shelf.setShelfName("Shelf-A");
        shelf.setPartNumber("PN-456");
        shelf.setIsDeleted(false);
        return shelf;
    }
    @Test
    void testCreateShelf() {
        Shelf shelf = createSampleShelf();
        shelf.setId(null);
        Shelf result = shelfService.createShelf(shelf);
        assertNotNull(result.getId());
        verify(shelfRepository, times(1)).createShelf(shelf);
    }
    @Test
    void testGetAllShelves() {
        Shelf shelf = createSampleShelf();
        when(shelfRepository.getAllShelves())
                .thenReturn(List.of(shelf));
        List<Shelf> shelves = shelfService.getAllShelves();
        assertEquals(1, shelves.size());
        assertEquals("Shelf-A", shelves.get(0).getShelfName());
        verify(shelfRepository, times(1)).getAllShelves();
    }
    @Test
    void testGetShelfByIdSuccess() {
        Shelf shelf = createSampleShelf();
        when(shelfRepository.getShelfById("101"))
                .thenReturn(shelf);
        Shelf result = shelfService.getShelfById("101");
        assertEquals("101", result.getId());
        assertEquals("Shelf-A", result.getShelfName());
    }
    @Test
    void testGetShelfByIdNotFound() {
        when(shelfRepository.getShelfById("101"))
                .thenReturn(null);
        assertThrows(ShelfNotFoundException.class, () ->
                shelfService.getShelfById("101")
        );
    }
    @Test
    void testUpdateShelfSuccess() {
        Shelf existing = createSampleShelf();
        Shelf updated = new Shelf();
        updated.setShelfName("UpdatedShelf");
        updated.setPartNumber("PN-999");
        when(shelfRepository.getShelfById("101"))
                .thenReturn(existing);
        shelfService.updateShelf("101", updated);
        assertEquals("UpdatedShelf", existing.getShelfName());
        assertEquals("PN-999", existing.getPartNumber());

        verify(shelfRepository).updateShelf(existing);
    }
    @Test
    void testUpdateShelfNotFound() {
        Shelf updated = createSampleShelf();
        when(shelfRepository.getShelfById("101"))
                .thenReturn(null);
        assertThrows(RuntimeException.class, () ->
                shelfService.updateShelf("101", updated)
        );
    }
    @Test
    void testDeleteShelfSuccess() {
        Shelf shelf = createSampleShelf();
        when(shelfRepository.getShelfById("101"))
                .thenReturn(shelf);
        shelfService.deleteShelf("101");
        verify(shelfRepository, times(1)).deleteShelf("101");
    }
    @Test
    void testDeleteShelfNotFound() {
        when(shelfRepository.getShelfById("101"))
                .thenReturn(null);
        assertThrows(RuntimeException.class, () ->
                shelfService.deleteShelf("101")
        );
    }
    @Test
    void testAssignShelfToShelfPosition() {
        Shelf shelf = createSampleShelf();
        when(shelfRepository.getShelfById("101"))
                .thenReturn(shelf);
        shelfService.assignShelf("101", "SP-1");
        verify(shelfRepository).assignShelftoShelfPosition("101", "SP-1");
    }
}
