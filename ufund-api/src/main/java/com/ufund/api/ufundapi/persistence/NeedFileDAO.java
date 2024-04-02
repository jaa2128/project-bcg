package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
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

    /**
     * Creates a list of needs with a null search term
     * @return An array of all needs (null search term)
     */
    private Need[] getNeedsArray() {
        return getNeedsArray(null, null, null, null);
    }

    /**
     * Creates a list of needs which contain a search term
     * @param containsText The search term to use
     * @return An array of all needs containing the search term
     */
    private Need[] getNeedsArray(String containsText, String type, String minContribution, String maxContribution) { // if containsText == null, don't filter any needs
        ArrayList<Need> needArrayList = new ArrayList<>();


        for (Need need : needs.values()) {
                if(type == null || type.isBlank() || need.getType().toLowerCase().equals(type.toLowerCase())){
                    if(minContribution == null || minContribution.isBlank() || need.getContributionPercent() >= Integer.valueOf(minContribution)){
                        if(maxContribution == null || maxContribution.isBlank() || need.getContributionPercent() <= Integer.valueOf(maxContribution)){
                            if (containsText == null ||containsText.isBlank() || (need.getName()).toLowerCase().contains(containsText.toLowerCase())) { //Use toLowerCase() to remove case sensitivity
                                needArrayList.add(need);
                            }
                        }
                    }
                }
            // if (containsText == null || (need.getName()).toLowerCase().contains(containsText.toLowerCase())) { //Use toLowerCase() to remove case sensitivity
            //     needArrayList.add(need);
            // }
        }

        Need[] needArray = new Need[needArrayList.size()];
        needArrayList.toArray(needArray);
        return needArray;
    }
    

    /**
     * Updates a single need object.
     * @param need The need object to update.
     * @return The updated need object.
     */
    @Override
    public Need updateNeed(Need need) throws IOException {
        synchronized(needs) {
            if (needs.containsKey(need.getID()) == false)
                return null;  // need does not exist
            needs.put(need.getID(),need);
            save(); // may throw an IOException
            return need;
        }
    }

/**
     * Creates a need that is identical to the need paramater
     * Puts the need into the needs Map
     * @param need   Need object to be duplicated
     * @return       The duplicate need
     */
    @Override
    public Need createNeed(Need need) throws IOException{
        synchronized(needs) {
            for (Need otherNeed : needs.values()){
                if(need.getName().equals(otherNeed.getName())){
                    return null;
                }
            }
            Need newNeed = new Need(nextId(), need.getName(), need.getDescription(), need.getType(), need.getTargetQuantity(), need.getAvailability());
            needs.put(newNeed.getID(),newNeed);
            save(); // may throw an IOException
            return newNeed;
        }
    }

    /**
     * Retrieves all current needs stored in the JSOn
     * @return all the needs stored in an array
     */
    @Override
    public Need[] getNeeds() throws IOException {
        synchronized(needs){
            return getNeedsArray();
        }
    }
    
    @Override
    public Need[] findNeeds(String containsText, String type, String minContribution, String maxContribution) throws IOException { 
        synchronized(needs) {
            return getNeedsArray(containsText, type, minContribution, maxContribution); //Use getNeedsArray to create an array of needs matching an input String
        }
    }
    
    /**
     * gets a need that matches id paramater
     * @param id   id of Need object to be found
     * @return     The need that was found
     */
    @Override
    public Need getNeed(int id) throws IOException { 
        synchronized(needs){
            if (needs.containsKey(id))
                return needs.get(id);
            else
                return null;
        }
     }

    /**
     * deletes a need that matches an id paramater
     * @param id   id of Need object to be deleted
     * @return     Whether or not the need was successfully deleted
     */
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

     /**
      * contributes to a need by the specified amount
      * @param id   the id of the need to contribute to
      * @param quantity the amount to contribute to the need by
      * @return     null if the need with the given id does not exist, otherwise the updated need
      */
     @Override
     public Need contributeNeed(int id, double quantity) throws IOException {
        synchronized(needs) {
            Need need = needs.get(id);
            if(need == null) { return null; }

            need.contribute(quantity);
            save(); //may throw an IOException
            return need;
        }
     }
}