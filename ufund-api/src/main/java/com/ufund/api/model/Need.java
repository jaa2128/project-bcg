package com.ufund.api.model;

public class Need {
    private int id;
    private String name;
    private String description;
    private double quantity; //double for prices
    private String type; //the type of thing that is requested

    public Need(int id, String name, String description, double quantity, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.type = type;
    }


}
