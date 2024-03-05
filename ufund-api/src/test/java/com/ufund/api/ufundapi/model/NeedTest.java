package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Need;

public class NeedTest {
    private Need need;

    @BeforeEach
    public void setupNeed() {
        need = new Need(50, "hello", "this is a description", "money", 99.99);
    }

    @Test
    public void testGetID() {
        int expected = 50;
        assertEquals(need.getID(), expected);
    }

    @Test
    public void testGetName() {
        String expected = "hello";
        assertEquals(need.getName(), expected);
    }

    @Test
    public void testGetDescription() {
        String expected = "this is a description";
        assertEquals(need.getDescription(), expected);
    }

    @Test
    public void testGetType() {
        String expected = "money";
        assertEquals(need.getType(), expected);
    }

    @Test
    public void testGetTargetQuantity() {
        double expected = 99.99;
        assertEquals(need.getTargetQuantity(), expected);
    }
    
    @Test
    public void testGetCurrentQuantity() {
        double expected = 0.0;
        assertEquals(need.getCurrentQuantity(), expected);
    }

    @Test
    public void testSetName() {
        String expected = "newHello";
        need.setName(expected);
        assertEquals(need.getName(), expected);
    }

    @Test
    public void testSetDescription() {
        String expected = "this is not a description";
        need.setDescription(expected);
        assertEquals(need.getDescription(), expected);
    }

    @Test
    public void testSetTargetQuantity() {
        double expected = 199.99;
        need.setTargetQuantity(expected);
        assertEquals(need.getTargetQuantity(), expected);
    }

    @Test
    public void testContribute() {
        double expected = 10.0;
        need.contribute(10.0);
        assertEquals(need.getCurrentQuantity(), expected);

        expected = 30.5;
        need.contribute(20.5);
        assertEquals(need.getCurrentQuantity(), expected);
    }

    @Test
    public void testIsSatisfiedTrue() {
        boolean expected = true;
        need.contribute(99.99);
        assertEquals(need.isSatisfied(), expected);
    }

    @Test
    public void testIsSatisfiedFalse() {
        boolean expected = false;
        need.contribute(39.99);
        assertEquals(need.isSatisfied(), expected);
    }

    @Test
    public void testToString() {
        String expected = "Need [id=50, name=hello, description=this is a description, type=money, targetQuantity=99.990000, currentQuantity=0.000000]";
        assertEquals(need.toString(), expected);
    }
}
