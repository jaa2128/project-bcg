package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test 
    public void testGetUserNull() throws IOException{
        User user = userFileDAO.getUser("Nonexistent");

        assertNull(user);
    }

    @Test
    public void testAuthenticateUser() throws IOException{
        User user = userFileDAO.getUser("Brandon");

        assertTrue(userFileDAO.authenticateUser(user.getUsername(), user.getPassword()));
    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test
    public void testAuthenticateUserFalse() throws IOException{
        User user = userFileDAO.getUser("Brandon");

        assertFalse(userFileDAO.authenticateUser(user.getUsername(), "Wrong Password"));
    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test
    public void testGetNeedsNull() throws IOException{
        assertNull(userFileDAO.getNeeds("Non Existent Username"));
    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test 
    public void testGetNeeds() throws IOException{
        User user = userFileDAO.getUser("Brandon");

        assertNotNull(userFileDAO.getNeeds(user.getUsername()));
    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test
    public void testGetContributions() throws IOException{
        User user = userFileDAO.getUser("Brandon");

        assertNotNull(userFileDAO.getContributions(user.getUsername()));
    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test
    public void testGetContributionsNull() throws IOException{
        assertNull(userFileDAO.getContributions("Non Existent Username"));
    }

    @Test
    public void testCreateUser() throws IOException{
        User newUser = new User("Tim", "Hansen");

        User result = userFileDAO.createUser(newUser.getUsername(), newUser.getPassword());

        assertNotNull(result);

    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test
    public void testCreateUserNull() throws IOException{
        // Have a user with the same username as another user
        User newUser = new User("Brandon", "Skibidi");

        User result = userFileDAO.createUser(newUser.getUsername(), newUser.getPassword());

        assertNull(result);
    }

    @Test
    public void testAddNeed() throws IOException{
        User newUser = testUsers[0];
        int newNeedID = 0;

        userFileDAO.addNeed(newUser.getUsername(), newNeedID, 10);

        assertNotNull(newUser.getNeeds().get(0));
    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test
    public void testAddNeedNull() throws IOException{
        int newNeedID = 1;

        assertNull(userFileDAO.addNeed("Non Existent Username", newNeedID, 10));
    }

    /**
     * @author Shaher Naser
     * @throws IOException
     */
    @Test
    public void testEditNeedNull() throws IOException {
        assertNull(userFileDAO.editNeed("Non Existent Username", 4, 5));
    }

    /**
     * @author Shaher Naser
     * @throws IOException
     */
    @Test
    public void testEditNeedSuccess() throws IOException {
        int actual = userFileDAO.editNeed("Brandon", 4, 5);
        assertEquals(actual, -1);
    }

    @Test
    public void testRemoveNeed() throws IOException{
        // Set up
        User newUser = testUsers[0];
        int newNeedID = 0;
        newUser.addNeed(newNeedID, 10);
        assertEquals(1, newUser.getNeeds().size());
        
        // Invoke
        userFileDAO.removeNeed(newUser.getUsername(), newNeedID);

        // Analyze
        assertEquals(0, newUser.getNeeds().size());
    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test 
    public void testRemoveNeedNull() throws IOException{
        // Set up
        User newUser = testUsers[0];
        int newNeedID = 0;
        newUser.addNeed(newNeedID, 10);
        assertEquals(1, newUser.getNeeds().size());
        assertEquals(1, newUser.getContributions().size());

        assertNull(userFileDAO.removeNeed("Non Existent Username", 0));

        assertEquals(1, newUser.getNeeds().size());
        assertEquals(1, newUser.getContributions().size());
    }

    @Test
    public void testClearBasket()throws IOException{
        User newUser = testUsers[0];
        int newNeed1ID = 0;
        int newNeed2ID = 1;
        int newNeed3ID = 2;

        newUser.addNeed(newNeed1ID, 3);
        newUser.addNeed(newNeed2ID, 3);
        newUser.addNeed(newNeed3ID, 3);

        assertTrue(userFileDAO.clearBasket(newUser.getUsername()));
        assertEquals(newUser.getNeeds().size(), 0);
        assertEquals(newUser.getContributions().size(), 0);
    }

    /**
     * @author Julian Alvia
     * @throws IOException
     */
    @Test
    public void testClearBasketFalse() throws IOException{
        User newUser = testUsers[0];

        int newNeed1ID = 0;
        int newNeed2ID = 1;
        int newNeed3ID = 2;

        newUser.addNeed(newNeed1ID, 3);
        newUser.addNeed(newNeed2ID, 3);
        newUser.addNeed(newNeed3ID, 3);

        assertFalse(userFileDAO.clearBasket("Non Existent Username"));
        assertEquals(newUser.getNeeds().size(), 3);
        assertEquals(newUser.getContributions().size(), 3);
    }

    @Test
    public void testChangePasswordSuccess() throws IOException {
        User newUser = testUsers[0];
        assertTrue(userFileDAO.changePassword(newUser.getUsername(), "abcdef"));
    }

    @Test
    public void testChangePasswordFailed() throws IOException {
        User newUser = testUsers[0];
        assertFalse(userFileDAO.changePassword(newUser.getUsername(), null));
    }

    @Test
    public void testChangeUsernameSuccess() throws IOException {
        User newUser = testUsers[0];
        assertTrue(userFileDAO.changeUsername(newUser.getUsername(), "anotherUsername"));
    }

    @Test
    public void testChangeUsernameFailed() throws IOException {
        User newUser = testUsers[0];
        assertFalse(userFileDAO.changeUsername(newUser.getUsername(), "admin"));
    }

}
