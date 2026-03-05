package com.example.assignment.controller;

import com.example.assignment.model.Shelf;

import com.example.assignment.service.ShelfService;

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

@WebMvcTest(ShelfController.class)

class ShelfControllerTest {

    @Autowired

    private MockMvc mockMvc;

    @MockBean

    private ShelfService shelfService;

    @Autowired

    private ObjectMapper objectMapper;

    private Shelf createSampleShelf() {

        Shelf shelf = new Shelf();

        shelf.setId("101");

        shelf.setShelfName("Shelf-A");

        shelf.setPartNumber("PN-456");

        shelf.setIsDeleted(false);

        return shelf;

    }

    @Test

    void testCreateShelf() throws Exception {

        Shelf shelf = createSampleShelf();

        when(shelfService.createShelf(shelf)).thenReturn(shelf);

        mockMvc.perform(post("/api/shelves/create")

                        .contentType("application/json")

                        .content(objectMapper.writeValueAsString(shelf)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.shelfName").value("Shelf-A"));

    }

    @Test

    void testGetAllShelves() throws Exception {

        Shelf shelf = createSampleShelf();

        when(shelfService.getAllShelves()).thenReturn(List.of(shelf));

        mockMvc.perform(get("/api/shelves/getAllShelves"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.size()").value(1))

                .andExpect(jsonPath("$[0].shelfName").value("Shelf-A"));

    }

    @Test

    void testGetShelfById() throws Exception {

        Shelf shelf = createSampleShelf();

        when(shelfService.getShelfById("101")).thenReturn(shelf);

        mockMvc.perform(get("/api/shelves/101"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value("101"))

                .andExpect(jsonPath("$.shelfName").value("Shelf-A"));

    }

    @Test

    void testUpdateShelf() throws Exception {

        Shelf shelf = createSampleShelf();

        doNothing().when(shelfService).updateShelf("101", shelf);

        mockMvc.perform(put("/api/shelves/101")

                        .contentType("application/json")

                        .content(objectMapper.writeValueAsString(shelf)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.shelfName").value("Shelf-A"));

    }

    @Test

    void testDeleteShelf() throws Exception {

        doNothing().when(shelfService).deleteShelf("101");

        mockMvc.perform(delete("/api/shelves/101"))

                .andExpect(status().isOk())

                .andExpect(content().string("true"));

    }

    @Test

    void testAssignShelf() throws Exception {

        doNothing().when(shelfService).assignShelf("101", "SP-1");

        mockMvc.perform(post("/api/shelves/101/assign/SP-1"))

                .andExpect(status().isOk())

                .andExpect(content().string("Shelf assigned successfully"));

    }

}
