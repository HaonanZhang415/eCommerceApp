package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        User user = new User();
        user.setId(0);
        user.setUsername("username");
        user.setPassword("password");

        Item item = new Item();
        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(15.99));
        item.setName("flash drive");
        item.setDescription("usb 3.0");
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setItems(itemList);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(15.99));
        user.setCart(cart);
        when(userRepository.findByUsername("username")).thenReturn(user);
    }

    @Test
    public void orderSubmission(){
        ResponseEntity<UserOrder> userOrderResponse = orderController.submit("username");
        assertEquals(BigDecimal.valueOf(15.99), userOrderResponse.getBody().getTotal());
        assertEquals(1, userOrderResponse.getBody().getItems().size());
        assertEquals(null, userOrderResponse.getBody().getId());
    }

    @Test
    public void userNotFound(){
        ResponseEntity<UserOrder> userOrderResponse = orderController.submit("wrongname");
        assertEquals(404, userOrderResponse.getStatusCodeValue());
    }
}
