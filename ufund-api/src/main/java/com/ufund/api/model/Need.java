package com.ufund.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class Need {
    @JsonProperty("id") private final int ID;
    @JsonProperty("name") private String name;
    @JsonProperty("description") private String description;
    @JsonProperty("type") private String type;
    @JsonProperty("targetQuantity") private double targetQuantity;
    @JsonProperty("currentQuantity") private double currentQuantity;


    /**
     * Creates a new Need with the given information
     * NOTE: All Needs are created with a currentQuantity of 0
     * (i.e. they haven't been fulfilled at all yet)
     * @param id the ID of the Need; cannot be changed once instantiated
     * @param name the displayed title for the Need
     * @param description a short description of what the Need is for
     * @param type what is requested by the Need
     * @param quantity the amount of the given type that is requested
     */
    public Need(int id, String name, String description, String type, double targetQuantity) {
        this.ID = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.targetQuantity = targetQuantity;
        this.currentQuantity = 0;
    }
    
    //GETTERS

    /*
     * retrieves the id of the Need
     */
    public int getID() { return ID; }

    /*
     * retrieves the name of the Need
     */
    public String getName() { return name; }

    /*
     * retrieves the description of the Need
     */
    public String getDescription() { return description; }

    /*
     * retrieves the type of thing requested
     */
    public String getType() { return type; }

    /*
     * retrieves how much of the type is required
     */
    public double getTargetQuantity() { return targetQuantity; }

    /*
     * retrieves how much of the type has been fulfilled so far
     */
    public double getCurrentQuantity() { return currentQuantity; }
}
