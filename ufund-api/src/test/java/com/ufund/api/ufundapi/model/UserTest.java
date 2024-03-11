
// package com.ufund.api.ufundapi.model;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertSame;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.Mockito.mock;

// import java.lang.reflect.Executable;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.NoSuchElementException;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import com.ufund.api.ufundapi.model.User;

// public class UserTest{

//     private User user;
//     private Need need1, need2, need3;

//     @BeforeEach
//     public void setupObjects(){
//         user = new User("GriddyMaster", "pass");
//         need1 = new Need(0, "Need1", "Need1", "Money", 10);
//         need2 = new Need(1, "Need2", "Need2", "Money", 200);
//         need3 = new Need(2, "Need3", "Need3", "Money", 500);
//     }
   
//     /**
//      * @author Brandon Santore
//      */
//     @Test
//     public void testAccessors(){
//         HashMap<Need, Double> testBasket = new HashMap<Need, Double>();
//         user.addNeed(need1, 100.0);
//         user.addNeed(need2, 50.0);
//         user.addNeed(need3, 35.0);
//         testBasket.put(need1, 100.0);
//         testBasket.put(need2, 50.0);
//         testBasket.put(need3, 35.0);

//         assertEquals(user.getUsername(), "GriddyMaster", "Why this no worky?!");
//         assertEquals(user.getBasket(), testBasket, "These baskets do not match!");

//     }


//     /**
//      * @author Brandon Santore
//      */
//     @Test
//     public void isAdminTest(){
//         User adminUser = new User("admin", "banana");
//         User helperUser = new User("Batman", "masterGriddy");
//         assertTrue(adminUser.isAdmin());
//         assertFalse(helperUser.isAdmin());

//     }

//     /**
//      * @author Alexander DiMartino
//      */
//     @Test
//     public void testAddNeed() {
//         user.addNeed(need1, 50);
//         assertNotNull(user.getBasket().get(need1));
//     }

//     /**
//      * @author Alexander DiMartino
//      */
//     @Test
//     public void testRemoveNeedSuccess() {
//         user.addNeed(need1, 50);
//         user.removeNeed(need1);
//         assertNull(user.getBasket().get(need1));
//     }

//     /**
//      * @author Alexander DiMartino
//      */
//     @Test
//     public void testRemoveNeedFailure() {
//         user.addNeed(need1, 50);
//         try {
//             user.removeNeed(need2);
//             assert(false);
//         }
//         catch(NoSuchElementException e) {
//             assert(true);           
//         }
//     }

//     /**
//      * @author Shaher Naser
//      */
//     @Test
//     public void testCheckout() {
//         //setup
//         user.addNeed(need1, 50);
//         user.addNeed(need2, 35);
//         user.addNeed(need3, 500);

//         //invoke
//         user.checkout();

//         //analyze
//         assertEquals(need1.getCurrentQuantity(), 50);
//         assertEquals(need2.getCurrentQuantity(), 35);
//         assertEquals(need3.getCurrentQuantity(), 500);
//     }

//     /**
//      * @author Shaher Naser
//      */
//     @Test
//     public void testClearBasket() {
//         //setup
//         user.addNeed(need1, 50);
//         user.addNeed(need2, 35);
//         user.addNeed(need3, 500);

//         //invoke
//         user.clearBasket();

//         //analyze
//         assertEquals(user.getBasket().size(), 0);
//     }
// }
