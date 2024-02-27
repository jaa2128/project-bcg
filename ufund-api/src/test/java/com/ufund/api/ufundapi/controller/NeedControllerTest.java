package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.persistence.NeedFileDAO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the need Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedDAO mockNeedDAO;
    private Need[] mockNeeds;

    /**
     * Before each test, create a new NeedController object and inject
     * a mock Need DAO.
     * The mock Need DAO will also need a mock database.
     */
    @BeforeEach
    public void setupNeedController() throws IOException {
        mockNeedDAO = mock(NeedDAO.class);
        needController = new NeedController(mockNeedDAO);
        mockNeeds = new Need[5];
        for(int i = 0; i < 5; i++) {
            mockNeeds[i] = mock(Need.class);
        }
    }

    @Test
    public void testGetNeed() throws IOException {  // getNeed may throw IOException
        // Setup
        Need need = new Need(99,"Test Need 1","Test Description","Test Type",1.0);
        // When the same id is passed in, our mock Need DAO will return the Need object
        when(mockNeedDAO.getNeed(need.getID())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(need.getID());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // createNeed may throw IOException
        // Setup
        int needID = 99;
        // When the same id is passed in, our mock Need DAO will return null, simulating
        // no need found
        when(mockNeedDAO.getNeed(needID)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needID);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createNeed may throw IOException
        // Setup
        int needID = 99;
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeed(needID);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needID);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all NeedController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateNeed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(99,"Test Need 2","Test Description","Test Type",1.0);
        // when createNeed is called, return true simulating successful
        // creation and save
        when(mockNeedDAO.createNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testCreateNeedFailed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(99,"Test Need 3","Test Description","Test Type",1.0);
        // when createNeed is called, return false simulating failed
        // creation and save
        when(mockNeedDAO.createNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(99,"Test Need 4","Test Description","Test Type",1.0);

        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).createNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(99,"Test Need 5","Test Description","Test Type",1.0);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);

        // when mockNeedDAO tries to access its needs, it returns a Need[]
        when(mockNeedDAO.getNeeds()).thenReturn(mockNeeds);

        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setName("Updated Test Need 5");

        // Invoke
        response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(99,"Test Need 6","Test Description","Test Type",1.0);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(null);

        // when mockNeedDAO tries to access its needs, it returns a Need[]
        when(mockNeedDAO.getNeeds()).thenReturn(mockNeeds);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(99,"Test Need 7","Test Description","Test Type",1.0);
        // When updateNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).updateNeed(need);

        // when mockNeedDAO tries to access its needs, it returns a Need[]
        when(mockNeedDAO.getNeeds()).thenReturn(mockNeeds);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws IOException { // getNeeds may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(99,"Test Need 8","Test Description","Test Type",1.0);
        needs[1] = new Need(100,"Test Need 9","Test Description","Test Type",1.0);
        // When getNeeds is called return the needs created above
        when(mockNeedDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { // getNeeds may throw IOException
        // Setup
        // When getNeeds is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchNeeds() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "la";
        Need[] needs = new Need[2];
        needs[0] = new Need(99,"Test Need 10","Test Description","Test Type",1.0);
        needs[1] = new Need(100,"Test Need 11","Test Description","Test Type",1.0);
        // When findNeeds is called with the search string, return the two
        // needs above
        when(mockNeedDAO.findNeeds(searchString)).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testSearchNeedsHandleException() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "an";
        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).findNeeds(searchString);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needID = 99;
        // when deleteNeed is called return true, simulating successful deletion
        when(mockNeedDAO.deleteNeed(needID)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needID);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needID = 99;
        // when deleteNeed is called return false, simulating failed deletion
        when(mockNeedDAO.deleteNeed(needID)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needID);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needID = 99;
        // When deleteNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).deleteNeed(needID);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needID);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
