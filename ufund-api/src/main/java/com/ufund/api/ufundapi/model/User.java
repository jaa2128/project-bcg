package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

public class User {
    private final String username; //User's username for login
    private final String password; //User's password
    private final ArrayList<Need> needs;
    private final ArrayList<Double> contributions;
    private final boolean isAdmin; // Whether user is a helper or an admin

    /**
     * Contructor for new instance of User
     * @param username //Will be determined by user during signup
     * @param basket //Initialized as empty ArrayList
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.needs = new ArrayList<Need>();
        this.contributions = new ArrayList<Double>();
        isAdmin = username.equals("admin");
    }

    /**
     * Retrieves User's username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Retrieves User's needs in funding basket
     */
    public ArrayList<Need> getNeeds(){
        return this.needs;
    }

    /**
     * Retrieves User's contributions to needs
     * in funding basket
     */
    public ArrayList<Double> getContributions(){
        return this.contributions;
    }

    /**
     * Retrieves User's funding basket
     */
    public void getBasket(){
        getNeeds();
        getContributions();
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
        this.needs.add(need);
        this.contributions.add(quantity);
    }

    /**
     * Removes a need from the user's funding basket
     * @param need the Need to remove from the basket
     * @throws NoSuchElementException if the need was not in the basket
     */
    public void removeNeed(Need need) throws NoSuchElementException {
        if(needs.remove(need) == null)
            throw new NoSuchElementException();
    }

    /**
     * Checks out all the needs in the funding basket
     */
    public void checkout() {
        for(Need need : this.needs){
            int i = this.needs.indexOf(need);
            need.contribute(this.contributions.get(i));
            
        }
    }

    /**
     * Clears all needs in the funding basket
     */
    public void clearBasket() {
        this.needs.clear();
        this.contributions.clear();
    }
}



