package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.persistence.UserFileDAO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the User Controller
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;
    private User[] mockUsers;

    @BeforeEach
    public void setupUserController() throws IOException{
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
        mockUsers = new User[5];
        for(int i = 0; i < 5; i++) {
            mockUsers[i] = mock(User.class);
        }
    }

    @Test
    public void testGetUser() throws IOException { // getNeed may throw IOException
        // Setup
        User user = new User("abc123", "1234");

        // When the same username is passed in, our mock User DAO will return the user
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getUsername(), "1234");

        // Analyze
        assertEquals(HttpStatus.OK, response);



    }

    @Test
    public void testClearBasket() throws IOException {
        //Setup
        User user = new User("abc123", "1234");
        //add some mock needs in basket
        for(int i = 0; i < 3; i++) {
            user.addNeed(mock(Need.class), 10.0);
        }
        //return the user when we try to call getUser()
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        //Invoke
        ResponseEntity<Boolean> response = userController.clearBasket(user.getUsername());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(user.getBasket().size(), 0);
    }

    
}
