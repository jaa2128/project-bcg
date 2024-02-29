package com.ufund.api.ufundapi.model;

import java.util.ArrayList;

public class User {
    private String username; //User's username for login
    private ArrayList<Need> basket; //User's funding basket
    private boolean isAdmin; // Whether user is a helper or an admin

    /**
     * Contructor for new instance of User
     * @param username //Will be determined by user during signup
     * @param basket //Initialized as empty ArrayList
     */
    public User(String username, ArrayList<Need> basket){
        username = this.username;
        basket = new ArrayList<>();
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
    public Boolean isAdmin(){
        return this.isAdmin;
    }
}



