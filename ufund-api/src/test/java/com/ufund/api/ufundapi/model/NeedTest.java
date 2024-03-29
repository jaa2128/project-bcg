package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NeedTest {

    private Need need;
    
    //setup
    @BeforeEach
    public void setupNeed() {
        boolean[] availability = {true, true, true, true, true, true, true};
        need = new Need(50, "hello", "this is a description", "money", 99.99, availability);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testGetID() {
        //invoke
        int actual = need.getID();
        
        //analyze
        int expected = 50;
        assertEquals(actual, expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testGetName() {
        //invoke
        String actual = need.getName();

        //analyze
        String expected = "hello";
        assertEquals(actual, expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testGetDescription() {
        //invoke
        String actual = need.getDescription();

        //analyze
        String expected = "this is a description";
        assertEquals(actual, expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testGetType() {
        //invoke
        String actual = need.getType();

        //analyze
        String expected = "money";
        assertEquals(actual, expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testGetTargetQuantity() {
        //invoke
        double actual = need.getTargetQuantity();

        //analyze
        double expected = 99.99;
        assertEquals(actual, expected);
    }
    
    /**
     * @author Shaher Naser
     */
    @Test
    public void testGetCurrentQuantity() {
        //invoke
        double actual = need.getCurrentQuantity();

        //analyze
        double expected = 0.0;
        assertEquals(actual, expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testSetName() {
        //invoke
        need.setName("newHello");
        String actual = need.getName();

        //analyze
        String expected = "newHello";
        assertEquals(actual, expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testSetDescription() {
        //invoke
        need.setDescription("this is not a description");
        String actual = need.getDescription();

        //analyze
        String expected = "this is not a description";
        assertEquals(actual, expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testSetTargetQuantity() {
        //invoke
        need.setTargetQuantity(199.99);
        double actual = need.getTargetQuantity();

        //analyze
        double expected = 199.99;
        assertEquals(need.getTargetQuantity(), expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testContribute() {
        //invoke
        need.contribute(10.0);
        double actual = need.getCurrentQuantity();
        
        //analyze
        double expected = 10.0;
        assertEquals(actual, expected);

        //invoke
        need.contribute(20.5);
        actual = need.getCurrentQuantity();

        //analyze
        expected = 30.5;
        assertEquals(need.getCurrentQuantity(), expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testIsSatisfiedTrue() {
        //invoke
        need.contribute(99.99);
        boolean actual = need.isSatisfied();
        
        //analuze
        boolean expected = true;
        assertEquals(actual, expected);
    }

    /**
     * @author Shaher Naser
     */
    @Test
    public void testIsSatisfiedFalse() {
        //invoke
        need.contribute(39.99);
        boolean actual = need.isSatisfied();

        //analyze
        boolean expected = false;
        assertEquals(need.isSatisfied(), expected);
    }

    @Test
    public void testHasEmptyField(){
        boolean[] availability = {true, true, true, true, true, true, true};
        Need emptyName = new Need(30, "", "Test Description", "Test Type", 1.0, availability);
        Need emptyDescription = new Need(30, "Test Need", "", "Test Type", 1.0, availability);
        Need emptyType = new Need(30, "Test Need", "Test Description", "", 1.0, availability);
        Need ZeroOrLessTQ = new Need(30, "Test Need", "Test Description", "Test Type", 0, availability);

        assertTrue(emptyName.hasEmptyField());
        assertTrue(emptyDescription.hasEmptyField());
        assertTrue(emptyType.hasEmptyField());
        assertTrue(ZeroOrLessTQ.hasEmptyField());
    }




    /**
     * @author Shaher Naser
     */
    @Test
    public void testToString() {
        //invoke
        String actual = need.toString();

        //analyze
        String expected = "Need [id=50, name=hello, description=this is a description, type=money, targetQuantity=99.990000, currentQuantity=0.000000]";
        assertEquals(actual, expected);
    }
}
