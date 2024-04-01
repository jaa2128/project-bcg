package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class User {
    @JsonProperty("username") private String username; //User's username for login
    @JsonProperty("password") private String password; //User's password
    @JsonProperty("needs") private ArrayList<Integer> needs; //list of needs in the user's basket
    @JsonProperty("contributions") private ArrayList<Double> contributions; //list of contribution amounts
    private final boolean isAdmin; // Whether user is a helper or an admin
    @JsonProperty("availability") private ArrayList<Boolean> availability;

    /**
     * Contructor for new instance of User
     * @param username //Will be determined by user during signup
     * @param password //Will be determined by user during signup
     */
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.username = username;
        this.password = password;
        this.needs = new ArrayList<Integer>();
        this.contributions = new ArrayList<Double>();
        this.isAdmin = username.equals("admin");
        this.availability = new ArrayList<Boolean>();
        for (int i = 0; i <= 6; i++) { this.availability.add(true); }
    }

    /**
     * Retrieves User's username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Changes the user's username
     * @param password
     */
    public void changeUsername(String newUsername) {
        this.username = newUsername; 
    }

    /**
     * Retrieves user's password (for testing purposes)
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Changes the user's password
     * @param password
     */
    public void changePassword(String newPassword) {
        this.password = newPassword; 
    }

    /**
     * Retrieves User's needs in funding basket
     */
    public ArrayList<Integer> getNeeds(){
        return this.needs;
    }

    /**
     * Retrieves User's contributions to needs
     * in funding basket
     */
    public ArrayList<Double> getContributions(){
        return this.contributions;
    }

    public ArrayList<Boolean> getAvailability(){
        return this.availability;
    }

    public void setAvailability(ArrayList<Boolean> availability){
        Collections.copy(this.availability, availability);
    }

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
     * @param needID the id ofID the Need to add to the funding basket
     * @param quantity the amount the user wants to contribute to the need
     */
    public void addNeed(int needID, double quantity) {
        int i = needs.indexOf(needID);
        if(i != -1) {
            contributions.set(i, contributions.get(i) + quantity);
        }
        else {
            this.needs.add(needID);
            this.contributions.add(quantity);
        }
    }

    /**
     * edits an existing need in the basket
     * @param needID the id of the need to edit
     * @param quantity the new quantity to be associated with the need
     * @return -1 if the need id does not exist in the basket
     * @return the id of the need if the quantity was successfully updated
     */
    public int editNeed(int needID, double quantity) {
        int i = needs.indexOf(needID);
        if(i == -1) {
            return -1;
        }
        else {
            contributions.set(i, quantity);
            return needID;
        }
        

    }

    /**
     * Removes a need from the user's funding basket
     * @param needID the id of the Need to remove from the basket
     * @return true if the need was removed
     * @return false if the need does not exist
     */
    public boolean removeNeed(int needID) {
        // loop through list
        for(int i = 0; i < needs.size(); i++)
        {
            // if the ids match up
            if(needs.get(i) == needID)
            {
                //remove the need along with the 
                //contribution associated with it
                this.needs.remove(i);
                this.contributions.remove(i);
                return true;
            }
        }

        // Otherwise if no ids match up 
        return false;
    }

    /**
     * Clears all needs in the funding basket
     */
    public void clearBasket() {
        this.needs.clear();
        this.contributions.clear();
    }
}



