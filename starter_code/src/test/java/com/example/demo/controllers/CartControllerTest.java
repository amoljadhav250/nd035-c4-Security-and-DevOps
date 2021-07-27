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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        TestUtils.injectObjects(cartController,"userRepository",userRepository);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepository);
    }

    @Test
    public void addToCartHappyPath(){
        Cart cart = new Cart();
        User user = new User(0,"Amol","amol1234",cart);
        when(userRepository.findByUsername("Amol")).thenReturn(user);

        Item item = createItem(1L, "Fidget Spinner", "Toy", new BigDecimal("5"));
        ModifyCartRequest modifyCartRequest = createCartRequest("Amol",1L,3);

        List<Item> itemArrayList = new ArrayList<>();
        itemArrayList.add(item);

        cart = createCart(1L,itemArrayList,user);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        Cart cart1 = response.getBody();

        //assertEquals(cart1.getUser().getUsername(),"Amol");
        assertEquals(cart1.getItems().size(),3);
    }

    @Test
    public void removeFromCartHappyPath(){
        Cart cart = new Cart();
        User user = new User(0,"Amol","amol1234",cart);
        when(userRepository.findByUsername("Amol")).thenReturn(user);

        Item item = createItem(1L, "Fidget Spinner", "Toy", new BigDecimal("5"));
        ModifyCartRequest modifyCartRequest = createCartRequest("Amol",1L,3);

        List<Item> itemArrayList = new ArrayList<>();
        itemArrayList.add(item);

        cart = createCart(1L,itemArrayList,user);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        Cart cart1 = response.getBody();
        assertEquals(cart1.getItems().size(),3);

        ModifyCartRequest modifyCartRequest2 = createCartRequest("Amol",1L,1);

        ResponseEntity<Cart> response2 = cartController.removeFromcart(modifyCartRequest2);

        assertNotNull(response2);
        assertEquals(200,response2.getStatusCodeValue());
        Cart cart2 = response2.getBody();
        assertEquals(cart2.getItems().size(),2);
    }

    private Cart createCart(long l, List<Item> itemArrayList, User user) {
        Cart cart = new Cart();
        cart.setId(l);
        cart.setItems(itemArrayList);
        cart.setUser(user);
        return cart;
    }


    private Item createItem(long l, String fidget_spinner, String toy, BigDecimal bigDecimal) {
        Item newItem = new Item();
        newItem.setId(l);
        newItem.setName(fidget_spinner);
        newItem.setDescription(toy);
        newItem.setPrice(bigDecimal);
        return newItem;
    }

    private ModifyCartRequest createCartRequest(String username, long l, int i) {
        ModifyCartRequest m = new ModifyCartRequest();
        m.setUsername(username);
        m.setItemId(l);
        m.setQuantity(i);
        return m;
    }
}
