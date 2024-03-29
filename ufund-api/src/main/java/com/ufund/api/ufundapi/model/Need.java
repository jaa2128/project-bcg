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
    @JsonProperty("starttime") String starttime;
    @JsonProperty("endtime") String endtime;

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
    @JsonProperty("targetQuantity") double targetQuantity,
    @JsonProperty("starttime") String starttime,
    @JsonProperty("endtime") String endtime ){
        this.ID = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.targetQuantity = targetQuantity;
        this.currentQuantity = 0;
        this.starttime = starttime;
        this.endtime = endtime;
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

    public String getStartTime() { return starttime; }

    public String getEndTime() { return endtime; }

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
     * updates the need's type
     * @param type
     */
    public void setType(String type){this.type = type;}
    
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

    public boolean areTimesValid(String starttime, String endtime){
        try {

            // check if colon is in the right spot (3rd character)
            if(starttime.charAt(2) != ':' || endtime.charAt(2) != ':'){ return false; }
            
            // extract hours and minutes from start and end times
            // also checks if numbers are in the right place, throws exception if not
            int intStartHours = Integer.parseInt(starttime.substring(0, 2));
            int intEndHours = Integer.parseInt(endtime.substring(0, 2));
            int intStartMinutes = Integer.parseInt(starttime.substring(3, 5));
            int intEndMinutes = Integer.parseInt(endtime.substring(3, 5));

            return true;

        } catch (Exception e) { return false; }
    }

    public boolean isStartTimeBeforeEndTime(String starttime, String endtime){
        try {
            
            // extract hours and minutes from start and end times
            // also checks if numbers are in the right place, throws exception if not
            int intStartHours = Integer.parseInt(starttime.substring(0, 2));
            int intEndHours = Integer.parseInt(endtime.substring(0, 2));
            int intStartMinutes = Integer.parseInt(starttime.substring(3, 5));
            int intEndMinutes = Integer.parseInt(endtime.substring(3, 5));

            // check if start times is less than end time (assuming start and end are on same day)
            if((intStartHours < intEndHours) || (intStartHours == intEndHours && intStartMinutes < intEndMinutes)){ return true; } else { return false; }

        } catch (Exception e) { return true; }
    }

    /**
     * String representation of a Need object to be used in JSON files
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, ID, name, description, type, targetQuantity, currentQuantity);
    }
}

