package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @Before
    public void setUp() throws Exception {

        orderController = new OrderController();
        userRepository = Mockito.mock(UserRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

    }

    @Test
    public void failGetOrdersForTheUser() {
        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setId(0l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));

        Cart cart = new Cart();
        cart.setId(0l);
        cart.setUser(user);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(199.99));

        user.setCart(cart);

        UserOrder userOrder = new UserOrder();
        userOrder.setId(0l);
        userOrder.setUser(user);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);
        Mockito.when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void failedSubmission() {
        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setId(0l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));

        Cart cart = new Cart();
        cart.setId(0l);
        cart.setUser(user);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(199.99));

        user.setCart(cart);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);
        final ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser() {
        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setId(0l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));

        Cart cart = new Cart();
        cart.setId(0l);
        cart.setUser(user);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(199.99));

        user.setCart(cart);

        UserOrder userOrder = new UserOrder();
        userOrder.setId(0l);
        userOrder.setUser(user);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(user);
        Mockito.when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> userOrders = response.getBody();

        assertNotNull(userOrders);
        assertEquals(user, userOrders.get(0).getUser());
    }

    @Test
    public void submission() {
        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        user.setPassword("testPassword");
        Item item = new Item();
        item.setId(0l);
        item.setName("iWatch");
        item.setPrice(BigDecimal.valueOf(199.99));

        Cart cart = new Cart();
        cart.setId(0l);
        cart.setUser(user);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(199.99));

        user.setCart(cart);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(user);
        final ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        assertNotNull(userOrder);
        assertEquals(user, userOrder.getUser());
    }



}
