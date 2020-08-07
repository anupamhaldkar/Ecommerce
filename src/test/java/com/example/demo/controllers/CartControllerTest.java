package com.example.demo.controllers;

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
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

public class CartControllerTest {

    private CartRepository cartRepository;
    private ItemRepository itemRepository;
    private CartController cartController;
    private UserRepository userRepository;


    @Before
    public void setUp() throws Exception {
        cartController = new CartController();
        userRepository = Mockito.mock(UserRepository.class);
        cartRepository = Mockito.mock(CartRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

    }

    @Test()
    public void failAddingToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        User user1 = new User();
        user1.setId(2l);
        user1.setUsername("test5");
        user1.setPassword("testPassword");
        Item item = new Item();
        item.setId(2l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));
        Cart cart1 = new Cart();
        cart1.setUser(user1);
        user1.setCart(cart1);
        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);
        Mockito.when(itemRepository.findById(2l)).thenReturn(Optional.of(item));
        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test()
    public void addingToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        User user1 = new User();
        user1.setId(1l);
        user1.setUsername("test5");
        user1.setPassword("testPassword");
        Item item = new Item();
        item.setId(1l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));
        Cart cart1 = new Cart();
        cart1.setUser(user1);
        user1.setCart(cart1);
        Mockito.when(userRepository.findByUsername("test")).thenReturn(user1);
        Mockito.when(itemRepository.findById(1l)).thenReturn(Optional.of(item));
        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart2 = response.getBody();
        User user2 = cart2.getUser();
        assertNotNull(cart2);
        assertNotNull(user2);
        assertEquals(user1, cart1.getUser());
        assertEquals(item, cart1.getItems().get(0));
    }

    @Test
    public void removingFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        user.setPassword("password");
        Item item = new Item();
        item.setId(1l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        Mockito.when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        Mockito.when(userRepository.findByUsername("test")).thenReturn(user);
        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart2 = response.getBody();
        assertNotNull(cart2);
        assertEquals(0, cart2.getItems().size());
    }

    @Test()
    public void failRemovingFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        user.setPassword("password");
        Item item = new Item();
        item.setId(1l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        Mockito.when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);
        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}