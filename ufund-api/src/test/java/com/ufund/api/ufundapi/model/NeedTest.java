package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NeedTest {

    private Need need;
    
    //setup
    @BeforeEach
    public void setupNeed() {
        need = new Need(50, "hello", "this is a description", "money", 99.99);
    }

    @Test
    public void testGetID() {
        //invoke
        int actual = need.getID();
        
        //analyze
        int expected = 50;
        assertEquals(actual, expected);
    }

    @Test
    public void testGetName() {
        //invoke
        String actual = need.getName();

        //analyze
        String expected = "hello";
        assertEquals(actual, expected);
    }

    @Test
    public void testGetDescription() {
        //invoke
        String actual = need.getDescription();

        //analyze
        String expected = "this is a description";
        assertEquals(actual, expected);
    }

    @Test
    public void testGetType() {
        //invoke
        String actual = need.getType();

        //analyze
        String expected = "money";
        assertEquals(actual, expected);
    }

    @Test
    public void testGetTargetQuantity() {
        //invoke
        double actual = need.getTargetQuantity();

        //analyze
        double expected = 99.99;
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetCurrentQuantity() {
        //invoke
        double actual = need.getCurrentQuantity();

        //analyze
        double expected = 0.0;
        assertEquals(actual, expected);
    }

    @Test
    public void testSetName() {
        //invoke
        need.setName("newHello");
        String actual = need.getName();

        //analyze
        String expected = "newHello";
        assertEquals(actual, expected);
    }

    @Test
    public void testSetDescription() {
        //invoke
        need.setDescription("this is not a description");
        String actual = need.getDescription();

        //analyze
        String expected = "this is not a description";
        assertEquals(actual, expected);
    }

    @Test
    public void testSetTargetQuantity() {
        //invoke
        need.setTargetQuantity(199.99);
        double actual = need.getTargetQuantity();

        //analyze
        double expected = 199.99;
        assertEquals(need.getTargetQuantity(), expected);
    }

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

    @Test
    public void testIsSatisfiedTrue() {
        //invoke
        need.contribute(99.99);
        boolean actual = need.isSatisfied();
        
        //analuze
        boolean expected = true;
        assertEquals(actual, expected);
    }

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
    public void testToString() {
        //invoke
        String actual = need.toString();

        //analyze
        String expected = "Need [id=50, name=hello, description=this is a description, type=money, targetQuantity=99.990000, currentQuantity=0.000000]";
        assertEquals(actual, expected);
    }
}
