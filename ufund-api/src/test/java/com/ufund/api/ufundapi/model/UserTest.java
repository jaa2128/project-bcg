package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.User;

public class UserTest{

    private User user;
    private Need need1, need2, need3;

    @BeforeEach
    public void setupObjects(){
        user = new User("GriddyMaster");
        need1 = new Need(0, "Need1", "Need1", "Money", 10);
        need2 = new Need(1, "Need2", "Need2", "Money", 200);
        need3 = new Need(2, "Need3", "Need3", "Money", 500);
    }
   
    // @Test
    // public void testAccessors(){
    //     ArrayList<Need> basket = new ArrayList<Need>();
    //     ArrayList<Need> testBasket = new ArrayList<Need>();
    //     basket.add(need1);
    //     basket.add(need2);
    //     basket.add(need3);
    //     testBasket.add(need1);
    //     testBasket.add(need2);
    //     testBasket.add(need3);
    //     User user = new User("bms1276", basket);

    //     assertEquals(user.getUsername(), "bms1276", "Why this no worky?!");
    //     assertEquals(user.getBasket(), testBasket, "These baskets do not match!");

    // }

    // @Test
    // public void isAdminTest(){
    //     User adminUser = new User("admin", new ArrayList<Need>());
    //     User helperUser = new User("Batman", new ArrayList<Need>());
    //     assertTrue(adminUser.isAdmin());
    //     assertFalse(helperUser.isAdmin());

    // }

    // @Test
    // public void testAddNeed() {

    // }

    // @Test
    // public void testRemoveNeedSuccess() {

    // }

    // @Test
    // public void testRemoveNeedFailure() {

    // }

    @Test
    public void testCheckout() {
        //setup
        user.addNeed(need1, 50);
        user.addNeed(need2, 35);
        user.addNeed(need3, 500);

        //invoke
        user.checkout();

        //analyze
        assertEquals(need1.getCurrentQuantity(), 50);
        assertEquals(need2.getCurrentQuantity(), 35);
        assertEquals(need3.getCurrentQuantity(), 500);
    }

    @Test
    public void testClearBasket() {
        //setup
        user.addNeed(need1, 50);
        user.addNeed(need2, 35);
        user.addNeed(need3, 500);

        //invoke
        user.clearBasket();

        //analyze
        assertEquals(user.getBasket().size(), 0);
    }
}
