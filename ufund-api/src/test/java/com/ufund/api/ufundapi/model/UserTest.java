package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.User;

public class UserTest{

    @Test
    public void testConstructor(){
        User user = new User("GriddyMaster", new ArrayList<Need>());
        User sameUser = new User("GriddyMaster", new ArrayList<Need>());
        assertEquals(user, sameUser);
    }
   
    @Test
    public void testAccessors(){
        Need need1 = new Need(0, "Need1", "Need1", "Money", 100);
        Need need2 = new Need(0, "Need2", "Need2", "Money", 100);
        Need need3 = new Need(0, "Need3", "Need3", "Money", 100);
        ArrayList<Need> basket = new ArrayList<Need>();
        ArrayList<Need> testBasket = new ArrayList<Need>();
        basket.add(need1);
        basket.add(need2);
        basket.add(need3);
        testBasket.add(need1);
        testBasket.add(need2);
        testBasket.add(need3);
        User user = new User("bms1276", basket);

        assertEquals(user.getUsername(), "bms1276", "Why this no worky?!");
        assertEquals(user.getBasket(), testBasket, "These baskets do not match!");

    }

    @Test
    public void isAdminTest(){
        User adminUser = new User("admin", new ArrayList<Need>());
        User helperUser = new User("Batman", new ArrayList<Need>());
        assertTrue(adminUser.isAdmin());
        assertFalse(helperUser.isAdmin());

    }




}
