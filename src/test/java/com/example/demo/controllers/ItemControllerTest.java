package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository;

    @Before
    public void setUp() throws Exception {

        itemController = new ItemController();
        itemRepository = Mockito.mock(ItemRepository.class);
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

    }

    @Test
    public void getItemById() {
        Item item = new Item();
        item.setId(1l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));
        Mockito.when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(1l);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item2 = response.getBody();
        assertNotNull(item2);
        assertEquals("iWatch", item2.getName());
    }

    @Test
    public void getItems() {
        Item item1 = new Item();
        item1.setId(1l);
        item1.setName("iWatch");
        item1.setPrice(BigDecimal.valueOf(199.99));
        Item item2 = new Item();
        item2.setId(2l);
        item2.setName("iPad");
        item2.setPrice(BigDecimal.valueOf(250));
        List<Item> items = new ArrayList() {{
            add(item1);
            add(item2);
        }};

        Mockito.when(itemRepository.findAll()).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> itemList = response.getBody();
        assertNotNull(itemList);
    }

    @Test
    public void failGetItemById() {
        long itemId = 1l;

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        final ResponseEntity<Item> response = itemController.getItemById(itemId);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByName() {
        Item item = new Item();
        item.setId(1l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));
        List<Item> items = Arrays.asList(item);
        Mockito.when(itemRepository.findByName("iWatch")).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("iWatch");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> itemList = response.getBody();

        assertNotNull(itemList);
        assertEquals("iWatch", itemList.get(0).getName());
        assertEquals(1, itemList.size());
    }

    @Test
    public void failGettingItemsByName() {
        String item = "iWatch";

        Mockito.when(itemRepository.findByName(item)).thenReturn(Collections.emptyList());
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(item);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
