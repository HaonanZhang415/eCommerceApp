package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        Item pencil = new Item();
        pencil.setId(0L);
        pencil.setName("pencil");
        when(itemRepository.findById(0L)).thenReturn(Optional.of(pencil));

        List<Item> itemList = new ArrayList<>();
        itemList.add(pencil);
        when(itemRepository.findByName("pencil")).thenReturn(itemList);

        Item box = new Item();
        pencil.setName("box");
        List<Item> itemList1 = new ArrayList<>();
        itemList1.add(pencil);
        itemList1.add(box);
        when(itemRepository.findAll()).thenReturn(itemList1);
    }

    @Test
    public void findItemById(){
        ResponseEntity<Item> itemResponse = itemController.getItemById(0L);
        assertEquals(200, itemResponse.getStatusCodeValue());
    }

    @Test
    public void itemIdNotFound(){
        ResponseEntity<Item> itemResponse = itemController.getItemById(1L);
        assertEquals(404, itemResponse.getStatusCodeValue());
    }

    @Test
    public void findItemsByName(){
        ResponseEntity<List<Item>> itemResponse = itemController.getItemsByName("pencil");
        assertEquals(200, itemResponse.getStatusCodeValue());
        assertEquals(1, itemResponse.getBody().size());
    }

    @Test
    public void getAllItems(){
        ResponseEntity<List<Item>> itemResponse = itemController.getItems();
        assertEquals(200, itemResponse.getStatusCodeValue());
        assertEquals(2, itemResponse.getBody().size());
    }
}
