package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Need {
    //JSON FORMATTING
    private static final String STRING_FORMAT = "Need [id=%d, name=%s, description=%s, type=%s, targetQuantity=%f, currentQuantity=%f]";

    //FIELDS
    @JsonProperty("id") private final int ID;
    @JsonProperty("name") private String name;
    @JsonProperty("description") private String description;
    @JsonProperty("type") private String type;
    @JsonProperty("targetQuantity") private double targetQuantity;
    @JsonProperty("currentQuantity") private double currentQuantity;

    //CONSTRUCTOR
    /**
     * Creates a new Need with the given information
     * NOTE: All Needs are created with a currentQuantity of 0
     * (i.e. they haven't been fulfilled at all yet)
     * @param id the ID of the Need; cannot be changed once instantiated
     * @param name the displayed title for the Need
     * @param description a short description of what the Need is for
     * @param type what is requested by the Need
     * @param targetQuantity the amount of the given type that is requested
     */
    public Need(@JsonProperty("id") int id, 
    @JsonProperty("name") String name, 
    @JsonProperty("description") String description, 
    @JsonProperty("type") String type, 
    @JsonProperty("targetQuantity") double targetQuantity) {
        this.ID = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.targetQuantity = targetQuantity;
        this.currentQuantity = 0;
    }
    
    //ACCESSORS
    /**
     * retrieves the id of the Need
     */
    public int getID() { return ID; }

    /**
     * retrieves the name of the Need
     */
    public String getName() { return name; }

    /**
     * retrieves the description of the Need
     */
    public String getDescription() { return description; }

    /**
     * retrieves the type of thing requested
     */
    public String getType() { return type; }

    /**
     * retrieves how much of the type is required
     */
    public double getTargetQuantity() { return targetQuantity; }

    /**
     * retrieves how much of the type has been fulfilled so far
     */
    public double getCurrentQuantity() { return currentQuantity; }

    //MODIFIERS
    /**
     * updates the name of the Need
     * @param name the new name of the Need
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * updates the description of the Need
     * @param description the new description of the Need
     */
    public void setDescription(String description) { this.description = description; }
    
    /**
     * updates the target quantity of the Need
     * @param targetQuantity the new target quantity of the Need
     */
    public void setTargetQuantity(double targetQuantity) { this.targetQuantity = targetQuantity; }
    
    /**
     * contributes to the currentQuantity of the Need
     * @param quantity the additional quantity to be added
     */
    public void contribute(double quantity) { currentQuantity += quantity; }

    //OTHER METHODS
    /**
     * asserts whether the Need has been fully satisfied
     * @return true if the Need has been fully satisfied, false otherwise
     */
    public boolean isSatisfied() { return currentQuantity >= targetQuantity; }

    /**
     * Checks if there is an empty data field in the need
     * created by the user
     * @return    Whether need has an empty field
     */
    public boolean hasEmptyField(){
        if(!getName().isEmpty()){
            if(!getDescription().isEmpty()){
                if(!getType().isEmpty()){
                    if(getTargetQuantity() > 0.0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * String representation of a Need object to be used in JSON files
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, ID, name, description, type, targetQuantity, currentQuantity);
    }
}

