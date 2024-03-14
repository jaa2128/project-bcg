package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.User;

public class UserTest{

    private User user;
    private int need1ID, need2ID, need3ID;

    @BeforeEach
    public void setupObjects(){
        user = new User("GriddyMaster", "pass");
        need1ID = 0;
        need2ID = 1;
        need3ID = 2;
    }
   
    /**
     * @author Brandon Santore
     */
    @Test
    public void testAccessors(){
        HashMap<Need, Double> testBasket = new HashMap<Need, Double>();
        user.addNeed(need1, 100.0);
        user.addNeed(need2, 50.0);
        user.addNeed(need3, 35.0);
        testBasket.put(need1, 100.0);
        testBasket.put(need2, 50.0);
        testBasket.put(need3, 35.0);

        assertEquals(user.getUsername(), "GriddyMaster", "Why this no worky?!");
        assertEquals(user.getBasket(), testBasket, "These baskets do not match!");

    }


    /**
     * @author Brandon Santore
     */
    @Test
    public void isAdminTest(){
        User adminUser = new User("admin", "banana");
        User helperUser = new User("Batman", "masterGriddy");
        assertTrue(adminUser.isAdmin());
        assertFalse(helperUser.isAdmin());

    }

    /**
     * @author Alexander DiMartino
     * @author Julian Alvia (refactor)
     */
    @Test
    public void testAddNeed() {
        user.addNeed(need1ID, 50);
        assertNotNull(user.getNeeds().get(need1ID));
    }

    /**
     * @author Alexander DiMartino
     * @author Julian Alvia (refactor)
     */
    @Test
    public void testRemoveNeedSuccess() {
        // add one need then remove a need
        user.addNeed(need1ID, 50);
        user.removeNeed(need1ID);

        // needs list must be 0
        assertEquals(0, user.getNeeds().size());
    }

    /**
     * @author Alexander DiMartino
     * @author Julian Alvia (refactor)
     */
    @Test
    public void testRemoveNeedFailure() {
        user.addNeed(need1ID, 50);
        
        assertEquals(false, user.removeNeed(need2ID));
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testClearBasket() {
        //setup
        user.addNeed(need1ID, 50);
        user.addNeed(need2ID, 35);
        user.addNeed(need3ID, 500);

        //invoke
        user.clearBasket();

        //analyze
        assertEquals(user.getNeeds().size(), 0);
    }
}
