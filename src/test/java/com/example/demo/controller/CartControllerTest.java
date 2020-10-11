package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
        TestUtils.injectObject(cartController, "userRepository", userRepository);

        Cart cart = new Cart();
        User user = new User();
        user.setId(0L);
        user.setUsername("username");
        user.setPassword("password");
        user.setCart(cart);
        when(userRepository.findByUsername("username")).thenReturn(user);

        Item item = new Item();
        item.setId(0L);
        item.setName("pencil");
        item.setPrice(BigDecimal.valueOf(3.99));
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addItemToCart(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(0L);
        cartRequest.setQuantity(11);
        cartRequest.setUsername("username");

        ResponseEntity<Cart> cartResponse = cartController.addTocart(cartRequest);
        assertEquals(200, cartResponse.getStatusCodeValue());

        Cart cart = cartResponse.getBody();
        assertEquals(BigDecimal.valueOf(43.89), cart.getTotal());
    }

    @Test
    public void cartUserNotFound(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(0L);
        cartRequest.setQuantity(100);
        cartRequest.setUsername("wrongname");

        ResponseEntity<Cart> cartResponse = cartController.addTocart(cartRequest);
        assertEquals(404, cartResponse.getStatusCodeValue());
    }

    @Test
    public void cartItemNotFound(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(100);
        cartRequest.setUsername("username");

        ResponseEntity<Cart> cartResponse = cartController.addTocart(cartRequest);
        assertEquals(404, cartResponse.getStatusCodeValue());
    }

    @Test public void removeItemFromCart(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(0L);
        cartRequest.setQuantity(100);
        cartRequest.setUsername("username");

        ResponseEntity<Cart> cartResponse = cartController.addTocart(cartRequest);
        assertEquals(200, cartResponse.getStatusCodeValue());

        ModifyCartRequest cartRequestModified = new ModifyCartRequest();
        cartRequestModified.setItemId(0L);
        cartRequestModified.setQuantity(1);
        cartRequestModified.setUsername("username");

        cartResponse = cartController.removeFromcart(cartRequestModified);
        assertEquals(200, cartResponse.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(395.01), cartResponse.getBody().getTotal());
    }
}

