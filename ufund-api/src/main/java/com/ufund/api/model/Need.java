package com.ufund.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Need {
    @JsonProperty("id") private final int id;
    @JsonProperty("name") private String name;
    @JsonProperty("desc") private String description;
    @JsonProperty("type") private String type; //the type of thing that is requested
    @JsonProperty("quantity") private double quantity; //double for prices

    /**
     * Creates a new Need with the given information
     * @param id the ID of the Need; cannot be changed once instantiated
     * @param name the displayed title for the Need
     * @param description a short description of what the Need is for
     * @param type what is requested by the Need
     * @param quantity the amount of the given type that is requested
     */
    public Need(int id, String name, String description, String type, double quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.type = type;
    }
    
    //GETTERS
    public int getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getType() { return type; }

    public double getQuantity() { return quantity; }
}
