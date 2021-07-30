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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepository);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepository);
    }

    @Test
    public void submitOrderHappyPath(){

        Item item = new Item(1L, "Round Widget", BigDecimal.valueOf(2.99), "A widget that is round");
        Item item2 = new Item(2L, "Square Widget", BigDecimal.valueOf(1.99), "A widget that is square");
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        Cart cart = new Cart();
        User user = new User(1L,"Amol","amol1234",cart);
        user.setCart(new Cart(1L,items,user));
        when(userRepository.findByUsername("Amol")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("Amol");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertEquals(user, order.getUser());
        assertEquals(items, order.getItems());

    }

    @Test
    public void getOrdersForUserHappyPath(){
        Item item = new Item(1L, "Round Widget", BigDecimal.valueOf(2.99), "A widget that is round");
        Item item2 = new Item(2L, "Square Widget", BigDecimal.valueOf(1.99), "A widget that is square");
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        Cart cart = new Cart();
        User user = new User(1L,"Amol","amol1234",cart);
        user.setCart(new Cart(1L,items,user));
        when(userRepository.findByUsername("Amol")).thenReturn(user);

        /*ResponseEntity<UserOrder> response = orderController.submit("Amol");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertEquals(user, order.getUser());
        assertEquals(items, order.getItems());*/

        ResponseEntity<List<UserOrder>>  response2 = orderController.getOrdersForUser("Amol");
        assertNotNull(response2);
        assertEquals(200,response2.getStatusCodeValue());
        List<UserOrder> userOrder = (List<UserOrder>) response2.getBody();
        System.out.println("userOrder:-"+userOrder);
        assertNotNull(userOrder);
    }

    @Test
    public void submitOrderNullUserSadPath(){
        when(userRepository.findByUsername("Amol")).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("Joan");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrderNullUserSadPath(){
        when(userRepository.findByUsername("Amol")).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Joan");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
