package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

public class User {
    private final String username; //User's username for login
    private final HashMap<Need, Double> basket; //User's funding basket
        //Keys are the needs
        //Values are the quantity to be contributed to the Need
    private final boolean isAdmin; // Whether user is a helper or an admin

    /**
     * Contructor for new instance of User
     * @param username //Will be determined by user during signup
     * @param basket //Initialized as empty ArrayList
     */
    public User(String username){
        this.username = username;
        this.basket = new HashMap<>();
        isAdmin = username.equals("admin");
    }

    /**
     * Retrieves User's username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Retrieves User's funding basket
     */
    public HashMap<Need, Double> getBasket(){
        return this.basket;
    }

    /**
     * Returns whether user is a helper or an admin
     * @return true is the user is an admin, false otherwise
     */
    public boolean isAdmin(){
        return this.isAdmin;
    }

    /**
     * Adds a need to the user's funding basket
     * @param need the Need to add to the funding basket
     * @param quantity the amount the user wants to contribute to the need
     */
    public void addNeed(Need need, double quantity) {
        basket.put(need, quantity);
    }

    /**
     * Removes a need from the user's funding basket
     * @param need the Need to remove from the basket
     * @throws NoSuchElementException if the need was not in the basket
     */
    public void removeNeed(Need need) throws NoSuchElementException {
        if(basket.remove(need) == null)
            throw new NoSuchElementException();
    }

    /**
     * Checks out all the needs in the funding basket
     */
    public void checkout() {
        for(Entry<Need, Double> entry : basket.entrySet()) {
            Need need = entry.getKey(); //the current need
            double quantity = entry.getValue(); //the quantity associated with need
            need.contribute(quantity);
        }
        clearBasket();
    }

    /**
     * Clears all needs in the funding basket
     */
    public void clearBasket() {
        basket.clear();
    }
}



