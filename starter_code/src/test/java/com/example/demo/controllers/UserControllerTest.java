package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepo);
        TestUtils.injectObjects(userController,"cartRepository",cartRepo);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
    }

    @Test
    public void createUserHappyPath(){
        when(encoder.encode("test_password")).thenReturn("thisIsHashed");


        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("test_password");
        createUserRequest.setConfirmPassword("test_password");

        ResponseEntity<User> response= userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test",user.getUsername());
        assertEquals("thisIsHashed",user.getPassword());
    }

    @Test
    public void findUserByNameHappyPath(){
        when(encoder.encode("test_password")).thenReturn("thisIsHashed");


        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("test_password");
        createUserRequest.setConfirmPassword("test_password");

        ResponseEntity<User> response= userController.createUser(createUserRequest);
        System.out.println("response="+response);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test",user.getUsername());
        //assertEquals("thisIsHashed",user.getPassword());

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<User> response2 = userController.findByUserName("test");
        System.out.println("response2="+response2);
        User user2=response2.getBody();
        //assertNotNull(user2);
        assertEquals(0,user2.getId());
        assertEquals("test",user2.getUsername());
        assertEquals("thisIsHashed",user2.getPassword());

    }

    @Test
    public void findUserByIdHappyPath(){
        when(encoder.encode("test_password")).thenReturn("thisIsHashed");


        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("test_password");
        createUserRequest.setConfirmPassword("test_password");

        ResponseEntity<User> response= userController.createUser(createUserRequest);
        System.out.println("response="+response);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test",user.getUsername());
        //assertEquals("thisIsHashed",user.getPassword());


        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<User> response2 = userController.findById(0L);
        System.out.println("response2="+response2);
        User user2=response2.getBody();
        //assertNotNull(user2);
        assertEquals(0,user2.getId());
        assertEquals("test",user2.getUsername());
        assertEquals("thisIsHashed",user2.getPassword());

    }

    @Test
    public void createUserSadPath(){
        when(encoder.encode("amol1234")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest("Amol","amol1234","amol12344");
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());

        User user=response.getBody();
        assertNull(user);
    }

    @Test
    public void findByIdSadPath(){
        ResponseEntity<User> response = userController.findById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findByUserNameSadPath(){
        ResponseEntity<User> response = userController.findByUserName("Amol");
        assertEquals(404, response.getStatusCodeValue());
    }
}
