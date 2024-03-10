package com.ufund.api.ufundapi.model;

import java.util.HashMap;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map.Entry;

public class User {
    @JsonProperty("username") private final String username; //User's username for login
    @JsonProperty("password") private final String password; //User's password
    @JsonProperty("basket") private final HashMap<Need, Double> basket; //User's funding basket
        //Keys are the needs
        //Values are the quantity to be contributed to the Need
    @JsonProperty("admin") private final boolean isAdmin; // Whether user is a helper or an admin

    /**
     * Contructor for new instance of User
     * @param username //Will be determined by user during signup
     * @param basket //Initialized as empty ArrayList
     */
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.username = username;
        this.password = password;
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

    public String getPassword() {return password;}

    /**
     * Returns whether user is a helper or an admin
     * @return true is the user is an admin, false otherwise
     */
    public boolean isAdmin(){
        return this.isAdmin;
    }

    /**
     * Verifies the password of the user
     * @param guess the guess for the password
     * @return true if the guess and password match, false otherwise
     */
    public boolean isPassword(String guess) {
        return password.equals(guess);
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
    }

    /**
     * Clears all needs in the funding basket
     */
    public void clearBasket() {
        basket.clear();
    }
}



