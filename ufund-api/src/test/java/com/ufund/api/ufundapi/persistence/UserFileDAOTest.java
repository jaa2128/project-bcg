package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;
    
    @BeforeEach
     public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        testUsers[0] = new User("Brandon", "Santore");
        testUsers[1] = new User("Fanum", "Toilet");
        testUsers[2] = new User("Skibidi", "Tax");

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),User[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetUsers() throws IOException{
        User[] users = userFileDAO.getUsers();
        assertEquals(testUsers.length, users.length);
        for(int i = 0; i<testUsers.length; i++){
            assertEquals(users[i], testUsers[i]);
        }
    }

    @Test
    public void testGetUser() throws IOException{
        User user = userFileDAO.getUser("Brandon");

        assertEquals("Brandon", user.getUsername());
    }

    @Test
    public void testAuthenticateUser() throws IOException{
        User user = userFileDAO.getUser("Brandon");

        assertTrue(user.isPassword(user.getPassword()));
    }

    @Test
    public void testCreateUser() throws IOException{
        User newUser = new User("Tim", "Hansen");

        User result = userFileDAO.createUser(newUser.getUsername(), newUser.getPassword());

        assertNotNull(result);

    }

    @Test
    public void testAddNeed() throws IOException{
        User newUser = testUsers[0];
        Need newNeed = new Need(20, "Test Need", "Test Need", "Test Type", 20);

        newUser.addNeed(newNeed, 10);

        assertTrue(newUser.getBasket().containsKey(newNeed));
    }

    @Test
    public void testRemoveNeed() throws IOException{
        User newUser = testUsers[0];
        Need newNeed = new Need(20, "Test Need", "Test Need", "Test Type", 20);

        newUser.addNeed(newNeed, 10);
        
        newUser.removeNeed(newNeed);
        assertFalse(newUser.getBasket().containsKey(newNeed));
    }

    @Test
    public void testClearBasket()throws IOException{
        User newUser = testUsers[0];
        Need newNeed1 = new Need(20, "Test Need", "Test Need", "Test Type", 20);
        Need newNeed2 = new Need(21, "Test Need", "Test Need", "Test Type", 20);
        Need newNeed3 = new Need(22, "Test Need", "Test Need", "Test Type", 20);

        newUser.addNeed(newNeed1, 3);
        newUser.addNeed(newNeed2, 3);
        newUser.addNeed(newNeed3, 3);

        newUser.clearBasket();;

        assertFalse(newUser.getBasket().containsKey(newNeed1));
        assertFalse(newUser.getBasket().containsKey(newNeed2));
        assertFalse(newUser.getBasket().containsKey(newNeed3));
    }

    @Test
    public void testCheckout() throws IOException{
        User newUser = testUsers[0];
        Need newNeed1 = new Need(20, "Test Need", "Test Need", "Test Type", 20);
        Need newNeed2 = new Need(21, "Test Need", "Test Need", "Test Type", 20);
        Need newNeed3 = new Need(22, "Test Need", "Test Need", "Test Type", 20);

        newUser.addNeed(newNeed1, 3);
        newUser.addNeed(newNeed2, 3);
        newUser.addNeed(newNeed3, 3);

        Set<Need> tempNeeds = newUser.getBasket().keySet();
        Need[] needs = (tempNeeds.toArray(new Need[tempNeeds.size()]));

        Need[] checkoutNeeds = userFileDAO.checkout(newUser.getUsername());

        

        for(int i = 0; i<checkoutNeeds.length; i++){
            assertEquals(checkoutNeeds[i], needs[i]);
        }

        
    }
}
