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

        Item item = new Item(1L, "Round Widget", new BigDecimal("5"),"Toy");
        ModifyCartRequest modifyCartRequest = createCartRequest("Amol",1L,3);

        List<Item> itemArrayList = new ArrayList<>();
        itemArrayList.add(item);

        cart = new Cart(1L,itemArrayList,user);

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

        Item item = new Item(1L, "Fidget Spinner",new BigDecimal("5"), "Toy");
        ModifyCartRequest modifyCartRequest = createCartRequest("Amol",1L,3);

        List<Item> itemArrayList = new ArrayList<>();
        itemArrayList.add(item);

        cart = new Cart(1L,itemArrayList,user);

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

    @Test
    public void addToCartNoItemSadPath(){
        Cart cart = new Cart();
        User user=new User(1L,"Amol","amol1234",cart);
        //Item item = new Item(1L,"Dog",BigDecimal.valueOf(50),"Animal");
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest("Amol", 1L,10);
        cart=new Cart(1L,null,user);

        when(userRepository.findByUsername("Amol")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartNoItemSadPath(){
        Cart cart = new Cart();
        User user=new User(1L,"Amol","amol1234",cart);
        //Item item = new Item(1L,"Dog",BigDecimal.valueOf(50),"Animal");
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest("Amol", 1L,10);
        cart=new Cart(1L,null,user);

        when(userRepository.findByUsername("Amol")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }


    private ModifyCartRequest createCartRequest(String username, long l, int i) {
        ModifyCartRequest m = new ModifyCartRequest();
        m.setUsername(username);
        m.setItemId(l);
        m.setQuantity(i);
        return m;
    }
}
