package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.persistence.UserFileDAO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
    public void testGetUsers() throws IOException { //getUsers may throw IOException
        // Setup
        when(mockUserDAO.getUsers()).thenReturn(mockUsers);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(response.getBody().length, mockUsers.length);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetUsersFail() throws IOException{
        // Set up 
        when(mockUserDAO.getUsers()).thenThrow(new IOException());
        // Invoke Should
        ResponseEntity<User[]> response = userController.getUsers();

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Test
    public void testGetUser() throws IOException { // getNeed may throw IOException
        // Setup
        User user1 = new User("abc123", "1234");
        User user2 = new User("", "5678");
        User user3 = new User("def456", "");

        // When the same username is passed in, our mock User DAO will return the user
        when(mockUserDAO.getUser(user1.getUsername())).thenReturn(user1);
        when(mockUserDAO.authenticateUser(user1.getUsername(), user1.getPassword())).thenReturn(true);

        when(mockUserDAO.getUser(user2.getUsername())).thenReturn(user2);
        when(mockUserDAO.authenticateUser(user2.getUsername(), user2.getPassword())).thenReturn(true);

        when(mockUserDAO.getUser(user3.getUsername())).thenReturn(user3);
        when(mockUserDAO.authenticateUser(user3.getUsername(), user3.getPassword())).thenReturn(true);

        // Invoke
        ResponseEntity<User> response1 = userController.getUser(user1.getUsername(), user1.getPassword());
        ResponseEntity<User> response2 = userController.getUser(user2.getUsername(), user2.getPassword());
        ResponseEntity<User> response3 = userController.getUser(user3.getUsername(), user3.getPassword());

        // Analyze
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
    }

    @Test 
    public void testGetUserNotFound() throws IOException {
        // Set up
        String userName = "Maurice";

        // When the same username is passed in, the mock User DAO will return null,
        // simulating no user found
        when(mockUserDAO.getUser(userName)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userName, "password");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserUnauthorized() throws IOException{
        // Set up
        User user = new User("Maurice", "password");

        // When the username is passed it the dao will return the user
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // When you attempt to authenticate with a different password return false
        when(mockUserDAO.authenticateUser(user.getUsername(), "1234")).thenReturn(false);

        // Invoke 
        ResponseEntity<User> response = userController.getUser(user.getUsername(), "1234");

        // Analyze 
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetUserIOException() throws IOException{
        //Setup
        User user1 = new User("abc123", "1234");
        
        //When Username is passed, the mockUserDAO will throw an IOException
        when(mockUserDAO.getUser(user1.getUsername())).thenThrow(new IOException());

        //Invoke
        ResponseEntity<User> response = userController.getUser(user1.getUsername(), user1.getPassword());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);;


    }

    @Test
    public void testClearBasket() throws IOException {
        //Setup
        User user = new User("abc123", "1234");

        //add some mock needID in basket
        for(int i = 0; i < 3; i++) {
            user.addNeed(i, 10.0);
        }
        //return the user when we try to call getUser()
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        //return true when the basket is cleared
        when(mockUserDAO.clearBasket(user.getUsername())).thenReturn(true);

        //Invoke
        ResponseEntity<Boolean> response = userController.clearBasket(user.getUsername());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testClearBasketNotFound() throws IOException{
        //Set up
        User user = new User("abc123", "1234");
        //add some mock needID in basket
        for(int i = 0; i < 3; i++) {
            user.addNeed(i, 10.0);
        }

        // return null when we call get user on a non-existent user
        when(mockUserDAO.getUser("Non-Existent User")).thenReturn(null);
        //return false when the basket is cleared
        when(mockUserDAO.clearBasket("Non-Existent User")).thenReturn(false);

        // Invoke
        ResponseEntity<Boolean> response = userController.clearBasket("Non-Existent User");

        //Analyze 
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(user.getNeeds().size(), 3);
    }   

    @Test
    public void testClearBasketError() throws IOException{
        // Set up
        // throw IOException when clearing basket 
        when(mockUserDAO.clearBasket("User")).thenThrow(new IOException());

        //Invoke 
        ResponseEntity<Boolean> response = userController.clearBasket("User");

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    @Test
    public void testGetNeeds() throws IOException{
        //Setup
        Need need1 = new Need(0, "Test Name 1", "Test Description 1", "Test Type 1", 100);
        Need need2 = new Need(1, "Test Name 2", "Test Description 2", "Test Type 2", 100);
        User user = new User("abc123", "1234");
        user.addNeed(need1.getID(), 50);
        user.addNeed(need2.getID(), 25);
        ArrayList<Integer> expectedNeeds = user.getNeeds();

        //When username is passed, the mockUserDAO will return the user's list of needs
        when(mockUserDAO.getNeeds(user.getUsername())).thenReturn(expectedNeeds);

        //Invoke
        ResponseEntity<ArrayList<Integer>> expectedResponse = userController.getNeeds(user.getUsername());

        //Analyze
        assertEquals(expectedResponse.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void testGetNeedsNotFound() throws IOException{
        //Setup
        User user = new User("abc123", "1234");

        //When username is passed, the mockUserDAO will return null
        when(mockUserDAO.getNeeds(user.getUsername())).thenReturn(null);

        //Invoke
        ResponseEntity<ArrayList<Integer>> expectedResponse = userController.getNeeds(user.getUsername());

        //Analyze
        assertEquals(expectedResponse.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateUser() throws IOException{
        //Setup
        User user = new User("user","password");

        //When user is created, the mockUserDAO will return the new user object
        when(mockUserDAO.createUser("user", "password")).thenReturn(user);

        //Invoke
        ResponseEntity<User> expectedResponse = userController.createUser("user", "password");
        
        //Analyze
        assertEquals(expectedResponse.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void testCreateUserConflict() throws IOException{
        when(mockUserDAO.createUser("user", "password2")).thenReturn(null);
        userController.createUser("user", "password");
        ResponseEntity<User> expectedResponse = userController.createUser("user", "password2");
        assertEquals(expectedResponse.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void testCreateUserServerError() throws IOException{
        when(mockUserDAO.createUser("user", "password")).thenThrow(new IOException());
        ResponseEntity<User> expectedResponse = userController.createUser("user", "password");
        assertEquals(expectedResponse.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testCreateUserBadRequest() throws IOException{
        //Setup
        User user1 = new User("abc123","");
        User user2 = new User("", "1234");
        
        //When user is created, the mockUserDAO will return the new user
        when(mockUserDAO.createUser("abc123", "")).thenReturn(user1);
        when(mockUserDAO.createUser("", "1234")).thenReturn(user2);
        
        //Invoke
        ResponseEntity<User> expectedResponse1 = userController.createUser("abc123", "");
        ResponseEntity<User> expectedResponse2 = userController.createUser("", "1234");

        //Analyze
        assertEquals(expectedResponse1.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(expectedResponse2.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

    /**
     * @author Shaher Naser
     * @throws IOException
     */
    @Test
    public void testEditNeedBadRequest() throws IOException {
        ResponseEntity<Integer> response = userController.editNeed("abc123", 5, -1);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    /**
     * @author Shaher Naser
     * @throws IOException
     */
    @Test
    public void testEditNeedNeedNotFound() throws IOException {
        User user = new User("abc", "123");
        for(int i = 0; i < 3; i++) {
            user.addNeed(i, 10.0);
        }
        when(mockUserDAO.editNeed(user.getUsername(), 6, 5)).thenReturn(-1);

        ResponseEntity<Integer> response = userController.editNeed(user.getUsername(), 6, 5);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /**
     * @author Shaher Naser
     * @throws IOException
     */
    @Test
    public void testEditNeedUserNotFound() throws IOException {
        User user = new User("abc", "123");
        for(int i = 0; i < 3; i++) {
            user.addNeed(i, 10.0);
        }
        when(mockUserDAO.editNeed("def", 2, 5)).thenReturn(null);

        ResponseEntity<Integer> response = userController.editNeed("def", 2, 5);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }


    /**
     * @author Shaher Naser
     * @throws IOException
     */
    @Test
    public void testEditNeedSuccess() throws IOException {
        User user = new User("abc", "123");
        for(int i = 0; i < 3; i++) {
            user.addNeed(i, 10.0);
        }
        when(mockUserDAO.editNeed(user.getUsername(), 2, 5)).thenReturn(2);

        ResponseEntity<Integer> response = userController.editNeed(user.getUsername(), 2, 5);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), 2);
    }

    /**
     * @author Shaher Naser
     * @throws IOException
     */
    @Test
    public void testEditNeedServerError() throws IOException{
        when(mockUserDAO.editNeed("user", 5, 6)).thenThrow(new IOException());
        ResponseEntity<Integer> expectedResponse = userController.editNeed("user", 5, 6);
        assertEquals(expectedResponse.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void removeNeed() throws IOException{
        // Set up
        User user = new User("abc123", "1234");
        //add some mock needID in basket
        for(int i = 0; i < 3; i++) {
            user.addNeed(i, 10.0);
        }

        // when removing need return true
        when(mockUserDAO.removeNeed(user.getUsername(), 0)).thenReturn(true);

        // Remove the need as though it worked
        user.removeNeed(0);

        //Invoke 
        ResponseEntity<Void> response = userController.removeNeed(user.getUsername(), 0);

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(user.getNeeds().size(), 2);
    }

    @Test
    public void removeNeedNull() throws IOException{
        //Setup
        User user = new User("abc123", "1234");
        Need need = new Need(0,"Name", "Description", "Type", 100);

        //When attempting to remove need, the mockUserDAO will return null
        when(mockUserDAO.removeNeed(user.getUsername(), need.getID())).thenReturn(null);

        //Invoke
        ResponseEntity<Void> response = userController.removeNeed(user.getUsername(), need.getID());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void removeNeedNotFound() throws IOException{
        //Setup
        User user = new User("abc123", "1234");
        Need need = new Need(0,"Name", "Description", "Type", 100);

        //When attempting to remove need, the mockUserDAO will return false
        when(mockUserDAO.removeNeed(user.getUsername(), need.getID())).thenReturn(false);

        //Invoke
        ResponseEntity<Void> response = userController.removeNeed(user.getUsername(), need.getID());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void removeNeedIOException() throws IOException{
        //Setup
        User user = new User("abc123", "1234");
        Need need = new Need(0,"Name", "Description", "Type", 100);

        //When attempting to remove need, the mockUserDAO will throw an IOException
        when(mockUserDAO.removeNeed(user.getUsername(), need.getID())).thenThrow(new IOException());

        //Invoke
        ResponseEntity<Void> response = userController.removeNeed(user.getUsername(), need.getID());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Test
    public void testGetNeedsIOException() throws IOException{
        //Setup
        User user = new User("abc123", "1234");

        //When username is passed, the mockUserDAO will throw an IOException
        when(mockUserDAO.getNeeds(user.getUsername())).thenThrow(new IOException());

        //Invoke
        ResponseEntity<ArrayList<Integer>> expectedResponse = userController.getNeeds(user.getUsername());

        //Analyze
        assertEquals(expectedResponse.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testGetContributions() throws IOException{
        //Setup
        User user = new User("abc123", "1234");
        Need need1 = new Need(0, "Name", "Description", "Type", 100);
        Need need2 = new Need(1, "Test", "Test", "Test", 100);
        user.addNeed(need1.getID(), 10);
        user.addNeed(need2.getID(), 30);
        
        //When username is passed, the mockUserDAO will return a list of the user's contributions
        when(mockUserDAO.getContributions(user.getUsername())).thenReturn(user.getContributions());

        //Invoke
        ResponseEntity<ArrayList<Double>> response = userController.getContributions(user.getUsername());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetContributionsNotFound() throws IOException{
        //Setup
        User user = new User("abc123", "1234");
        
        //When username is passed, the mockUserDAO will return null
        when(mockUserDAO.getContributions(user.getUsername())).thenReturn(null);

        //Invoke
        ResponseEntity<ArrayList<Double>> response = userController.getContributions(user.getUsername());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetContributionsIOException() throws IOException{
        //Setup
        User user = new User("abc123", "1234");

        //When username is passed, the mockUserDAO will throw an IOException
        when(mockUserDAO.getContributions(user.getUsername())).thenThrow(new IOException());

        //Invoke
        ResponseEntity<ArrayList<Double>> response = userController.getContributions(user.getUsername());

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Test
    public void testAddNeed() throws IOException{
        //Setup
        User user = new User("abc123", "1234");
        Need need = new Need(0, "Name", "Description", "Type", 100);

        //When username, need ID, and need quantity are passed, the mockUserDao will return the ID of the need
        when(mockUserDAO.addNeed(user.getUsername(), need.getID(), 10)).thenReturn(need.getID());

        //Invoke
        ResponseEntity<Integer> response = userController.addNeed(user.getUsername(), need.getID(), 10);

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        
    }

    @Test
    public void testAddNeedNotFound() throws IOException{
        //Setup
        User user = new User("abc123", "1234");

        //When username, need ID, and need quantity are passed, the mockUserDao will return null
        when(mockUserDAO.addNeed(user.getUsername(), 0, 10)).thenReturn(null);

        //Invoke
        ResponseEntity<Integer> response = userController.addNeed(user.getUsername(), 0, 10);

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        
    }

    @Test
    public void testAddNeedIOException() throws IOException{
        //Setup
        User user = new User("abc123", "1234");
        Need need = new Need(0, "Name", "Description", "Type", 100);

        //When username, need ID, and need quantity are passed, the mockUserDao will throw an IOException
        when(mockUserDAO.addNeed(user.getUsername(), need.getID(), 10)).thenThrow(new IOException());

        //Invoke
        ResponseEntity<Integer> response = userController.addNeed(user.getUsername(), need.getID(), 10);

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    @Test
    public void testAddNeedZeroOrLess() throws IOException{
        //Setup
        User user = new User("abc123", "1234");
        Need need = new Need(0, "Name", "Description", "Type", 100);

        //When username, need ID, and need quantity are passed, the mockUserDao will return the ID of the need
        when(mockUserDAO.addNeed(user.getUsername(), need.getID(), 0)).thenReturn(need.getID());

        //Invoke
        ResponseEntity<Integer> response = userController.addNeed(user.getUsername(), need.getID(), 0);

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        
    }

}