package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Need;

@Component
public class NeedFileDAO implements NeedDAO {
    
    private static final Logger LOG = Logger.getLogger(NeedFileDAO.class.getName());
    Map<Integer,Need> needs;   // Provides a local cache of need objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Need
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next ID to assign to a new need
    private String filename;    // Filename to read from and write to

    public NeedFileDAO(@Value("${needs.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the needs from the file
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private boolean save() throws IOException {
        Need[] needArray = getNeedsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),needArray);
        return true;
    }

    private boolean load() throws IOException {
        needs = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of needs
        // readValue will throw an IOException if there's an issue with the file
        Need[] needArray = objectMapper.readValue(new File(filename),Need[].class);

        // Add each need to the tree map and keep track of the greatest id
        for (Need need : needArray) {
            needs.put(need.getID(),need);
            if (need.getID() > nextId)
                nextId = need.getID();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    private Need[] getNeedsArray() {
        return getNeedsArray(null);
    }

    private Need[] getNeedsArray(String containsText) { // if containsText == null, don't filter any needs
        ArrayList<Need> needArrayList = new ArrayList<>();

        for (Need need : needs.values()) {
            if (containsText == null || need.getName().contains(containsText)) {
                needArrayList.add(need);
            }
        }

        Need[] needArray = new Need[needArrayList.size()];
        needArrayList.toArray(needArray);
        return needArray;
    }
    
    @Override
    public Need updateNeed(Need need) throws IOException {
        synchronized(needs) {
            if (needs.containsKey(need.getID()) == false)
                return null;  // hero does not exist

            needs.put(need.getID(),need);
            save(); // may throw an IOException
            return need;
        }
    }

    @Override
    public Need createNeed(Need need) throws IOException{
        synchronized(needs) {
            Need newNeed = new Need(nextId(), need.getName(), need.getDescription(), need.getType(), need.getTargetQuantity());
            needs.put(newNeed.getID(),newNeed);
            save(); // may throw an IOException
            return newNeed;
        }
    }

    @Override
    public Need[] getNeeds() throws IOException {
        return getNeedsArray();
    }
    
    @Override
    public Need[] findNeeds(String containsText) throws IOException {
        synchronized(needs){
            return getNeedsArray(containsText);
        }
     }
    
    @Override
    public Need getNeed(int id) throws IOException { 
        synchronized(needs){
            if (needs.containsKey(id))
                return needs.get(id);
            else
                return null;
        }
     }

    @Override
    public boolean deleteNeed(int id) throws IOException { 
        synchronized(needs){
            if(needs.containsKey(id)){
                needs.remove(id);
                return save();
            }
            else
                return false;
        }
     }

}