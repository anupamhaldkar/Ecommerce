package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() throws Exception {

        userController = new UserController();
        userRepository = Mockito.mock(UserRepository.class);
        cartRepository = Mockito.mock(CartRepository.class);
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

    }

    @Test
    public void failFindById() {
        String username = "test4";
        String password = "testPassword";

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        Mockito.when(userRepository.findById(0l)).thenReturn(Optional.empty());

        final ResponseEntity<User> response = userController.findById(0l);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void creatingUser() {

        String username = "test";
        String password = "testPassword";
        //Testing BCrypt
        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void failCreatingUser() {

        String username = "test";
        String password = "";
        //Without password
        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findById() {
        String username = "test3";
        String password = "testPassword";

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);

        Mockito.when(userRepository.findById(0l)).thenReturn(Optional.of(userResponseEntity.getBody()));

        final ResponseEntity<User> response = userController.findById(0l);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(userResponseEntity.getBody().getId(), user.getId());
    }

    @Test
    public void findByUserName() {

        String username = "test1";
        String password = "testPassword";

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(userResponseEntity.getBody());

        final ResponseEntity<User> response = userController.findByUserName(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(userResponseEntity.getBody().getId(), user.getId());
        assertEquals(userResponseEntity.getBody().getUsername(), user.getUsername());
        assertEquals(userResponseEntity.getBody().getPassword(), user.getPassword());
    }

    @Test
    public void failFindByUserName() {

        String username = "test2";
        String password = "testPassword";

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        final ResponseEntity<User> response = userController.findByUserName(username);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }




}
