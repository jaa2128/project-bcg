package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class User {
    private final String username; //User's username for login
    private final ArrayList<Need> basket; //User's funding basket
    private final boolean isAdmin; // Whether user is a helper or an admin

    /**
     * Contructor for new instance of User
     * @param username //Will be determined by user during signup
     * @param basket //Initialized as empty ArrayList
     */
    public User(String username){
        this.username = username;
        this.basket = new ArrayList<>();
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
    public ArrayList<Need> getBasket(){
        return this.basket;
    }

    /**
     * Returns whether user is a helper or an admin
     */
    public boolean isAdmin(){
        return this.isAdmin;
    }

    /**
     * Adds a need to the user's funding basket
     * @param need the Need to add to the funding basket
     */
    public void addNeed(Need need) {
        basket.add(need);
    }

    /**
     * Removes a need from the user's fundibg basket
     * @param need the Need to remove from the basket
     * @throws NoSuchElementException if the need was not in the basket
     */
    public void removeNeed(Need need) throws NoSuchElementException {
        if(!basket.remove(need))
            throw new NoSuchElementException(); 
    }


 
}



