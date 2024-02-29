package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Need;

public class NeedTest {    
    @Test
    public void testAccessors() {
        Need need = new Need(50, "hello", "this is a description", "money", 99.99);
        assertEquals(need.getID(), 50);
        assertEquals(need.getName(), "hello");
        assertEquals(need.getDescription(), "this is a description");
        assertEquals(need.getType(), "money");
        assertEquals(need.getTargetQuantity(), 99.99);
        assertEquals(need.getCurrentQuantity(), 0);
    }

    @Test
    public void testModifiers() {
        Need need = new Need(50, "hello", "this is a description", "money", 99.99);
        need.setName("newHello");
        assertEquals(need.getName(), "newHello");
        need.setDescription("this is not a description");
        assertEquals(need.getDescription(), "this is not a description");
        need.setTargetQuantity(199.99);
        assertEquals(need.getTargetQuantity(), 199.99);
    }

    @Test
    public void testIsSatisfied() {
        Need need = new Need(50, "hello", "this is a description", "money", 99.99);
        assertEquals(need.isSatisfied(), false);
        need.contribute(10000);
        assertEquals(need.isSatisfied(), true);
    }

    @Test
    public void testToString() {
        Need need = new Need(50, "hello", "this is a description", "money", 99.99);
        assertEquals(need.toString(),
        "Need [id=50, name=hello, description=this is a description, type=money, targetQuantity=99.990000, currentQuantity=0.000000]");
    }

    @Test
    public void testContribute() {
        Need need = new Need(50, "hello", "this is a description", "money", 99.99);
        need.contribute(50.3);
        need.contribute(10.25);
        assertEquals(need.getCurrentQuantity(), 60.55);
    }

    
}
