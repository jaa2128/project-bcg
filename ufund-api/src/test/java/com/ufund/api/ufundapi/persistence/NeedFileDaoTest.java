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
        Need[] needs = needFileDAO.findNeeds("ne", "", "", "", null);

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
        Need[] output = needFileDAO.findNeeds("world", "", "", "", null);

        //analyze
        assertTrue(Arrays.asList(output).contains(need1));
        assertTrue(Arrays.asList(output).contains(need2));
        assertFalse(Arrays.asList(output).contains(need3));
    }

    @Test
    public void testGetNeedsArray1() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Volunteer", 100, availability);
        Need[] needs = {need4};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("e", "Volunteer", "0", "50", testAvailability);

        assertTrue(Arrays.asList(result).contains(need4));

    }

    @Test
    public void testGetNeedsArray2() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Volunteer", 100, availability);
        Need[] needs = {need4};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("z", "", "", "", testAvailability);

        assertFalse(Arrays.asList(result).contains(need4));

    }

    @Test
    public void testGetNeedsArray3() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Other", 100, availability);
        Need[] needs = {need4};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("z", "Other", "", "", testAvailability);

        assertFalse(Arrays.asList(result).contains(need4));

    }

    @Test
    public void testGetNeedsArray4() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Monetary", 100, availability);
        need4.contribute(75);
        Need[] needs = {need4};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("", "", "0", "100", testAvailability);

        assertTrue(Arrays.asList(result).contains(need4));

    }

    @Test
    public void testGetNeedsArray5() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Monetary", 100, availability);
        need4.contribute(75);
        Need[] needs = {need4};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("", "", "0", "20", testAvailability);

        assertFalse(Arrays.asList(result).contains(need4));

    }

    @Test
    public void testGetNeedsArray6() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Other", 100, availability);
        need4.contribute(75);
        Need[] needs = {need4};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("", "", null, null, testAvailability);

        assertTrue(Arrays.asList(result).contains(need4));

    }

    @Test
    public void testGetNeedsArray7() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Other", 100, availability);
        Need need5 = new Need(51, "hello world", "testing", "Volunteer", 100, availability);
        Need need6 = new Need(52, "hello world", "testing", "Money", 100, availability);
        need4.contribute(75);
        Need[] needs = {need4, need5, need6};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("", "", null, null, testAvailability);

        assertTrue(Arrays.asList(result).contains(need4));
        assertTrue(Arrays.asList(result).contains(need5));
        assertTrue(Arrays.asList(result).contains(need6));

    }

    @Test
    public void testGetNeedsArray8() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Other", 100, availability);
        Need need5 = new Need(51, "hello world 2", "testing", "Volunteer", 100, availability);
        Need need6 = new Need(52, "hello world 21", "testing", "Money", 100, availability);
        Need need7 = new Need(53, "hello world 456", "testing", "Volunteer", 100, availability);
        Need need8 = new Need(54, "hello worlds", "testing", "Goods", 100, availability);
        Need need9 = new Need(55, "return of hello world", "testing", "Money", 100, availability);
        Need need10 = new Need(56, "among us", "testing", "Money", 100, availability);
        Need need11 = new Need(57, "hello world", "testing", "Goods", 100, availability);
        Need need12 = new Need(58, "hello world", "testing", "Other", 100, availability);
        Need need13 = new Need(59, "hello world", "testing", "Goods", 100, availability);
        need4.contribute(75);
        Need[] needs = {need4, need5, need6, need7, need8, need9, need10, need11, need12, need13};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("hello", "", null, null, testAvailability);

        assertTrue(Arrays.asList(result).contains(need4));
        assertTrue(Arrays.asList(result).contains(need5));
        assertTrue(Arrays.asList(result).contains(need6));
        assertTrue(Arrays.asList(result).contains(need7));
        assertTrue(Arrays.asList(result).contains(need8));
        assertTrue(Arrays.asList(result).contains(need9));
        assertFalse(Arrays.asList(result).contains(need10));
        assertTrue(Arrays.asList(result).contains(need11));
        assertTrue(Arrays.asList(result).contains(need12));
        assertTrue(Arrays.asList(result).contains(need13));
    }

    @Test
    public void testGetNeedsArray9() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need4 = new Need(50, "hello world", "testing", "Monetary", 100, availability);
        Need[] needs = {need4};

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("", "", "25", "100", testAvailability);

        assertFalse(Arrays.asList(result).contains(need4));

    }

    @Test
    public void testGetNeedsArrayOtherNeed() throws IOException{
        ArrayList<Boolean> availability = new ArrayList<Boolean>(); for (int i = 0; i < 7; i++) { availability.add(true); }
        Boolean[] testAvailability = {false, false, false, false, false, false, true};
        Need need = new Need(100, "Test", "test" , "Other", 100, availability);
        Need need2 = new Need(101, "Test", "test" , "Other", 100, availability);
        Need need3 = new Need(102, "Test", "test" , "Other", 100, availability);
        Need goods = new Need(103, "Test", "test" , "Goods", 100, availability);
        Need volunteer = new Need(104, "Test", "test" , "Volunteer", 100, availability);
        Need money = new Need(105, "Test", "test" , "Money", 100, availability);
        Need[] needs = {need, need2, need3, goods, volunteer, money};
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);

        needFileDAO.contributeNeed(100, 20);
        Need[] result = needFileDAO.findNeeds("Test", "Other", "15", "30", testAvailability);
        Need[] result2 = needFileDAO.findNeeds("", "Other", "", "", testAvailability);
        Need[] result3 = needFileDAO.findNeeds(null, "Other", null, null, testAvailability);
        assertTrue(Arrays.asList(result).contains(need));
        assertTrue(Arrays.asList(result2).contains(need2));
        assertTrue(Arrays.asList(result3).contains(need3));
        assertFalse(Arrays.asList(result).contains(goods));
        assertFalse(Arrays.asList(result).contains(volunteer));
        assertFalse(Arrays.asList(result).contains(money));

    }

   
    public void testGetNeedsArray10() throws IOException{
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {availability.add(true);}
        Need need5 = new Need(50, "hello world", "testing", "Volunteer", 10, availability);
        Need[] needs = {need5};
        Boolean[] testAvailabilty = {false, false, false, false, false, false, false};
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);

        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("", "Volunteer", "", "", testAvailabilty);

        assertFalse(Arrays.asList(result).contains(need5));
        assertTrue(result.length == 0);
    }
    @Test
    public void testGetNeedsArray12() throws IOException{
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {availability.add(true);}
        Need need5 = new Need(50, "hello world", "testing", "Volunteer", 10, availability);
        Need need6 = new Need(50, "Hello", "testing", "Money", 20, availability);
        Need[] needs = {need5, need6};
        Boolean[] testAvailabilty = {true, true, true, true, false, false, true};
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);

        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("", "Volunteer", null, null, testAvailabilty);

        assertFalse(Arrays.asList(result).contains(need5));
        assertFalse(Arrays.asList(result).contains(need6));
    }

    @Test
    public void testGetNeedsArray13() throws IOException{
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {availability.add(true);}
        Need need5 = new Need(50, "hello world", "testing", "Volunteer", 10, availability);
        Need need6 = new Need(51, "Hello", "testing", "Money", 20, availability);
        Need[] needs = {need5, need6};
        Boolean[] testAvailabilty = {true, true, true, true, false, false, true};
        need5.contribute(5);
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);

        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds(null, "", "0", "50", null);

        assertTrue(Arrays.asList(result).contains(need5));
        assertTrue(Arrays.asList(result).contains(need6));
    }

    @Test
    public void testGetNeedsArray14() throws IOException{
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {availability.add(true);}
        Need need5 = new Need(50, "hello world", "testing", "Volunteer", 10, availability);
        Need need6 = new Need(51, "Hello", "testing", "Money", 20, availability);
        Need[] needs = {need5, need6};
        Boolean[] testAvailabilty = {true, true, true, true, true, true, true};
        need5.contribute(7);
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);

        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("", "", "50", "100", testAvailabilty);

        assertTrue(Arrays.asList(result).contains(need5));
        assertFalse(Arrays.asList(result).contains(need6));
    }

    @Test
    public void testGetNeedsArray15() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {
            if(i % 2 == 0)
                availability.add(true);
            else
                availability.add(false);
        }
        Need need = new Need(1, "a", "b", "Volunteer", 10, availability);
        Need need2 = new Need(2, "c", "d", "Other", 20, availability);
        Need[] needs = {need, need2};
        Boolean[] testAvailability = {true, false, true, false, true, false, false};
        need.contribute(2);
        need2.contribute(19);
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("a", "Volunteer", "10", "30", testAvailability);

        assertTrue(Arrays.asList(result).contains(need));
        assertFalse(Arrays.asList(result).contains(need2));
    
    }

    @Test
    public void testGetNeedsArray16() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {
            if(i % 2 == 0)
                availability.add(true);
            else
                availability.add(false);
        }
        Need need = new Need(1, "a", "b", "Volunteer", 10, availability);
        Need need2 = new Need(2, "c", "d", "Other", 20, availability);
        Need[] needs = {need, need2};
        Boolean[] testAvailability = {true, false, true, false, true, false, false};
        need.contribute(2);
        need2.contribute(19);
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("a", "Volunteer", "30", "40", testAvailability);

        assertFalse(Arrays.asList(result).contains(need));
        assertFalse(Arrays.asList(result).contains(need2));
    
    }

    @Test
    public void testGetNeedsArray17() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {
            if(i % 2 == 0)
                availability.add(true);
            else
                availability.add(false);
        }
        Need need = new Need(1, "a", "b", "Volunteer", 10, availability);
        Need need2 = new Need(2, "c", "d", "Other", 20, availability);
        Need[] needs = {need, need2};
        Boolean[] testAvailability = {true, false, true, false, true, false, false};
        need.contribute(4);
        need2.contribute(19);
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("a", "Volunteer", "10", "30", testAvailability);

        assertFalse(Arrays.asList(result).contains(need));
        assertFalse(Arrays.asList(result).contains(need2));
    
    }

    @Test
    public void testGetNeedsArray18() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {
            if(i % 2 == 0)
                availability.add(true);
            else
                availability.add(false);
        }
        Need need = new Need(1, "a", "b", "Volunteer", 10, availability);
        Need need2 = new Need(2, "c", "d", "Other", 20, availability);
        Need[] needs = {need, need2};
        Boolean[] testAvailability = {true, false, true, false, true, false, false};
        need.contribute(4);
        need2.contribute(19);
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("a", "Volunteer", "50", null, testAvailability);

        assertFalse(Arrays.asList(result).contains(need));
        assertFalse(Arrays.asList(result).contains(need2));
    
    }


    @Test
    public void testGetNeedsArray19() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {
            if(i % 2 == 0)
                availability.add(true);
            else
                availability.add(false);
        }
        Need need = new Need(1, "a", "b", "Volunteer", 10, availability);
        Need need2 = new Need(2, "c", "d", "Other", 20, availability);
        Need[] needs = {need, need2};
        Boolean[] testAvailability = {true, false, true, false, true, false, false};
        need.contribute(4);
        need2.contribute(19);
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds("a", "Volunteer", "50", "", testAvailability);

        assertFalse(Arrays.asList(result).contains(need));
        assertFalse(Arrays.asList(result).contains(need2));
    
    }

    @Test
    public void testGetNeedsArray20() throws IOException {
        ArrayList<Boolean> availability = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {
            if(i % 2 == 0)
                availability.add(true);
            else
                availability.add(false);
        }
        Need need = new Need(1, "a", "b", "fnegfor", 10, availability);
        Need need2 = new Need(2, "c", "d", "vmnjdfiguefu", 20, availability);
        Need[] needs = {need, need2};
        Boolean[] testAvailability = {true, false, true, false, true, false, false};
        need.contribute(4);
        need2.contribute(19);
        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), Need[].class)).thenReturn(needs);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt", mockObjectMapper);

        Need[] result = needFileDAO.findNeeds(null, "Other", "30", "100", testAvailability);

        assertTrue(Arrays.asList(result).contains(need));
        assertTrue(Arrays.asList(result).contains(need2));
    
    }

}