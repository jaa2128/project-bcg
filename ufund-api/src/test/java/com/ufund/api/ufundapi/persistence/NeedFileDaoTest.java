package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

public class NeedFileDaoTest {

    NeedFileDAO needFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;


    public void setupNeedFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[3];
        testNeeds[0] = new Need(0, "Need1", "Donate Money", "Money", 1000);
        testNeeds[1] = new Need(1, "Need2", "Donate Controllers", "Physical Good", 10);
        testNeeds[2] = new Need(2, "Need3", "Volunteers Needed", "Volunteer", 20);

        
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void getNeedNotFound() throws IOException{
        assertEquals(needFileDAO.getNeed(100), null);

    }

    @Test
    public void getDeleteNeedNotFound() throws IOException{
        boolean result = needFileDAO.deleteNeed(100);

        assertEquals(result, false);
    }

    @Test
    public void getUpdateHeroNotFound() throws IOException{
        Need need = new Need(4, "Stinky", "Pick up trash", "Volunteer", 300);
        Need result = needFileDAO.updateNeed(need);

        assertNull(result);

    }

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
}