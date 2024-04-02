package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

public class NeedFileDaoTest {
    NeedFileDAO needFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;
    
    /**
     * Before each test, create and inject a Mock Object Mapper to 
     * isolate the tests from the underlying file
     * @throws IOException
     * @author Julian Alvia
     */
    @BeforeEach
    public void setupHeroFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[3];
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        testNeeds[0] = new Need(99, "Need1", "this is a description", "money", 99.99, availability);
        testNeeds[1] = new Need(100, "Need2", "this is a description", "money", 99.99, availability);
        testNeeds[2] = new Need(101, "Need3", "this is a description", "money", 99.99, availability);

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test 
    /**
     * @throws IOException
     * @author Julian Alvia
     */
    public void testGetNeeds() throws IOException{
        // Invoke 
        Need[] needs = needFileDAO.getNeeds();

        // Analyze
        assertEquals(needs.length,testNeeds.length);
        for (int i = 0; i < testNeeds.length;++i)
        assertEquals(needs[i],testNeeds[i]);
    }

    /**
     * 
     * @throws IOException
     * @author Julian Alvia
     */
    @Test
    public void testFindNeeds() throws IOException {
        // Invoke
        Need[] needs = needFileDAO.findNeeds("ne", "", "", "");

        // Analyze
        assertEquals(needs.length,3);
        assertEquals(needs[0],testNeeds[0]);
        assertEquals(needs[1],testNeeds[1]);
        assertEquals(needs[2],testNeeds[2]);
        }

    /** 
     * @throws IOException
     * @author Julian Alvia
     */
    @Test
    public void testGetNeed() throws IOException{
        // Invoke
        Need need = needFileDAO.getNeed(99);

        // Analzye
        assertEquals(need,testNeeds[0]);
    }

    /**
     * @throws IOException
     * @author Brandon Santore
     */
    @Test
    public void getNeedNotFound() throws IOException{
        assertEquals(needFileDAO.getNeed(3), null);

    }

    /**
     * @throws IOException
     * @author Brandon Santore
     */
    @Test
    public void getDeleteNeedNotFound() throws IOException{
        boolean result = needFileDAO.deleteNeed(3);

        assertEquals(result, false);
    }


    /** 
     * @author Alexander DiMartino
     */
    @Test
    public void testDeleteNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test heroes array - 1 (because of the delete)
        // Because heroes attribute of HeroFileDAO is package private
        // we can access it directly
        assertEquals(needFileDAO.needs.size(),testNeeds.length-1);
    }

     /**
     * @throws IOException
     * @author Brandon Santore
     */
    @Test
    public void getUpdateNeedNotFound() throws IOException{
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Need need = new Need(4, "Stinky", "Pick up trash", "Volunteer", 300, availability);
        Need result = needFileDAO.updateNeed(need);

        assertNull(result);

    }

    /** 
     * @throws IOException
     * @author Alexander DiMartino
     */
    @Test
    public void testCreateNeed() throws IOException {
        // Setup
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Need oldNeed = new Need(49, "oldTest", "testing", "money", 99.99, availability);
        Need need = new Need(50, "test", "testing", "money", 99.99, availability);
        Need conflictNeed = new Need(51, "test", "testing", "money", 99.99, availability);

        Need[] testNeeds = new Need[1];
        testNeeds[0] = oldNeed;

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);
   

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.createNeed(need));
        Need result2 = needFileDAO.createNeed(conflictNeed);

        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getID());
        assertEquals(actual.getID(),need.getID());
        assertEquals(actual.getName(),need.getName());
        assertNull(result2);
    }

    /**
     * @throws IOException
     * @author Brandon Santore
     */
    @Test
    public void testConstructorException() throws IOException{
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Need[].class);

        assertThrows(IOException.class,
                        () -> new NeedFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    /** 
     * @throws IOException
     * @author Alexander DiMartino
     */
    @Test
    public void testUpdateNeed() throws IOException {
        // Setup
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Need oldNeed = new Need(51, "oldTest", "testing", "money", 99.99, availability);
        Need need = new Need(51, "test", "testing", "money", 99.99, availability);
        Need[] testNeeds = new Need[1];
        testNeeds[0] = oldNeed;

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);
   
        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getID());
        assertEquals(actual,need);
    }

    /** 
     * @throws IOException
     * @author Alexander DiMartino
     */
    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Need[].class));

        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Need need = new Need(52, "test", "testing", "money", 99.99, availability);

        assertThrows(IOException.class,
                        () -> needFileDAO.createNeed(need),
                        "IOException not thrown");
    }


    /**
     * @throws IOException
     * @author Brandon Santore
     */
    @Test
    public void testContributeNeed() throws IOException{
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Need newNeed = new Need(1000, "newNeed", "NewNeed", "Money", 1000, availability);
        Need otherNeed = new Need(1001, "otherNeed", "otherNeed", "Money", 1000, availability);
        Need[] testNeeds = new Need[]{newNeed, otherNeed};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        needFileDAO.contributeNeed(newNeed.getID(), 500);

        assertNotEquals(newNeed.getCurrentQuantity(), otherNeed.getCurrentQuantity());
    }

    /**
     * @throws IOException
     * @author Brandon Santore
     */
    @Test
    public void testContributeNeedNotFound() throws IOException{
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Need newNeed = new Need(1000, "newNeed", "NewNeed", "Money", 1000, availability);

        Need result = needFileDAO.contributeNeed(newNeed.getID(), 500);

        assertNull(result);
    }

    @Test
    public void testSearchNeeds() throws IOException {
        //setup
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Need need1 = new Need(50, "hello world", "testing", "money", 99.99, availability);
        Need need2 = new Need(49, "GOODBYE WORLD", "testing", "money", 99.99, availability);
        Need need3 = new Need(51, "greetings universe", "testing", "money", 99.99, availability);
        Need[] testNeeds = new Need[]{need1, need2, need3};
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        //invoke
        Need[] output = needFileDAO.findNeeds("world", "", "", "");

        //analyze
        assertTrue(Arrays.asList(output).contains(need1));
        assertTrue(Arrays.asList(output).contains(need2));
        assertFalse(Arrays.asList(output).contains(need3));
    }

}