package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsHappyPath() {
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void getItemByIdHappyPath() {
        Item newItem = new Item(1L, "Doll", BigDecimal.valueOf(5),"It is a toy");
        when(itemRepository.findById(newItem.getId())).thenReturn(Optional.of(newItem));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item itemFound = response.getBody();
        assertNotNull(itemFound);
        assertEquals(Long.valueOf(1L), itemFound.getId());
        assertEquals("Fidget Spinner", itemFound.getName());
        assertEquals("Toy", itemFound.getDescription());
        assertEquals(BigDecimal.valueOf(5), itemFound.getPrice());
    }


    @Test
    public void getItemByNameHappyPath() {
        Item item1 = new Item(1L, "Fidget Spinner",BigDecimal.valueOf(5), "Metal Toy");
        Item item2 = new Item(1L, "Fidget Spinner", BigDecimal.valueOf(5),"Plastic Toy");
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        when(itemRepository.findByName("Fidget Spinner")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Fidget Spinner");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemsFound = response.getBody();
        assertNotNull(itemsFound);
        assertEquals(Long.valueOf(1L), itemsFound.get(0).getId());
        assertEquals("Fidget Spinner", itemsFound.get(0).getName());
        assertEquals("Metal Toy", itemsFound.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(5), itemsFound.get(0).getPrice());
    }

}
