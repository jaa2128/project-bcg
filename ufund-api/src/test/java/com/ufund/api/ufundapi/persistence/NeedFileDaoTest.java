package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOError;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
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
        testNeeds[0] = new Need(99, "Need1", "this is a description", "money", 99.99);
        testNeeds[1] = new Need(100, "Need2", "this is a description", "money", 99.99);
        testNeeds[2] = new Need(101, "Need3", "this is a description", "money", 99.99);

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
        Need[] needs = needFileDAO.findNeeds("ne");

        // Analyze
        assertEquals(needs.length,2);
        assertEquals(needs[0],testNeeds[1]);
        assertEquals(needs[1],testNeeds[2]);
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
    public void getUpdateNeedNotFound() throws IOException{
        Need need = new Need(4, "Stinky", "Pick up trash", "Volunteer", 300);
        Need result = needFileDAO.updateNeed(need);

        assertNull(result);

    }

    @Test
    public void testCreateNeed() throws IOException {
        // Setup
        Need need = new Need(50, "test", "testing", "money", 99.99);

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.createNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getID());
        assertEquals(actual.getID(),need.getID());
        assertEquals(actual.getName(),need.getName());
    public void testConstructorException() throws IOException{
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Need[].class);

        assertThrows(IOException.class,
                        () -> new NeedFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    @Test
    public void testUpdateNeed() throws IOException {
        // Setup
        Need need = new Need(51, "test", "testing", "money", 99.99);

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getID());
        assertEquals(actual,need);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Need[].class));

        Need need = new Need(52, "test", "testing", "money", 99.99);

        assertThrows(IOException.class,
                        () -> needFileDAO.createNeed(need),
                        "IOException not thrown");
    }
}