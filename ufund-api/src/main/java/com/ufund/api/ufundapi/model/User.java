package com.ufund.api.ufundapi.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("username") private final String username; //User's username for login
    @JsonProperty("password") private final String password; //User's password
    @JsonProperty("needs") private ArrayList<Need> needs; //list of needs in the user's basket
    @JsonProperty("contributions") private ArrayList<Double> contributions; //list of contribution amounts
    private final boolean isAdmin; // Whether user is a helper or an admin

    /**
     * Contructor for new instance of User
     * @param username //Will be determined by user during signup
     * @param password //Will be determined by user during signup
     */
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.username = username;
        this.password = password;
        this.needs = new ArrayList<Need>();
        this.contributions = new ArrayList<Double>();
        this.isAdmin = username.equals("admin");
    }

    /**
     * Retrieves User's username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Retrieves user's password (for testing purposes)
     */
    public String getPassword() {
        return this.password;
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
     * @return true if the need was removed
     * @return false if the need does not exist
     */
    public boolean removeNeed(Need need) {
        int i = this.needs.indexOf(need);
        if(i == -1) {
            return false;
        }
        else {
            //remove the need along with the 
            //contribution associated with it
            this.needs.remove(i);
            this.contributions.remove(i);
            return true;
        }
    }

    /**
     * Checks out all the needs in the funding basket
     */
    public void checkout() {
        for(int i = 0; i < this.needs.size(); i++) {
            Need need = this.needs.get(i);
            double quantity = this.contributions.get(i);
            need.contribute(quantity);
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



