package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

@Component
public class UserFileDAO implements UserDAO {
    
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());
    Map<String,User> users;   // Provides a local cache of user objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between User
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to

    public UserFileDAO(@Value("${users.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the needs from the file
    }

    private boolean save() throws IOException {
        User[] userArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
        return true;
    }

    private boolean load() throws IOException {
        users = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of users
        // readValue will throw an IOException if there's an issue with the file
        User[] userArray = objectMapper.readValue(new File(filename),User[].class);

        // Add each user to the tree map
        for (User user : userArray) {
            users.put(user.getUsername(),user);
        }

        return true;
    }

    /**
     * Creates a list of all users
     * @return An array of all users
     */
    private User[] getUsersArray() {
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            userArrayList.add(user);
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Retrieves all current users stored in the JSOn
     * @return all the users stored in an array
     */
    @Override
    public User[] getUsers() throws IOException {
        synchronized(users){
            return getUsersArray();
        }
    }

    /**
     * retrieves a specific user
     * @param username the username of the user
     * @return the User with the given username, or null if no such user exists
     */
    @Override
    public User getUser(String username) throws IOException {
        synchronized(users) {
            for(User user : users.values()) {
                if(user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * verifies the password of a user
     * @param username the username of the user
     * @param password the guessed password
     * @return true if the password matches
     * @return false if the password fails
     * 
     * NOTE: THIS METHOD ASSUMES THAT THE GIVEN USERNAME ALREADY EXISTS
     */
    @Override
    public boolean authenticateUser(String username, String password) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user.isPassword(password)) {
                return true;
            }
            return false;
        }
    }

    /**
     * Creates a new user
     * @param username the username of the new user
     * @return the new user if the given username was not already in use, null otherwise
     */
    @Override
    public User createUser(String username, String password) throws IOException {
        synchronized(users) {
            for (User user : users.values()){
                if(user.getUsername().equals(username)) {
                    return null;
                }
            }
            User newUser = new User(username, password);
            users.put(username, newUser);
            save(); // may throw an IOException
            return newUser;
        }
    }

    /**
     * adds a need to the user's funding basket
     * @param username the username of the user
     * @param need the need the user adds to the basket
     * @param quantity the amount to contribute to the need
     * @return the need if it was successfully added, null if the user doesn't exist
     */
    @Override
    public Need addNeed(String username, Need need, double quantity) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null) {
                return null;
            }
            HashMap<Need, Double> basket = user.getBasket();
            basket.put(need, quantity);
            save(); //may throw IOException
            return need;
        }
    }

    /**
     * Removes a need from the user basket
     */
    @Override
    public boolean removeNeed(String username, Need need) throws IOException {
        synchronized(users) {
            try {
                getUser(username).removeNeed(need);
                save();
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }
        }
    }

    /**
     * Checks out the user basket
     * @return The list of needs checked out
     */
    @Override
    public Need[] checkout(String username) throws IOException {
        synchronized(users){
            getUser(username).checkout();
            Set<Need> tempNeeds = getUser(username).getBasket().keySet();
            Need[] needs = (tempNeeds.toArray(new Need[tempNeeds.size()]));
            getUser(username).clearBasket();
            save();
            return needs; 
        }
    }

    /**
     * Clears the user basket
     */
    @Override
    public boolean clearBasket(String username) throws IOException {
        getUser(username).clearBasket();
        save();
        return getUser(username).getBasket().isEmpty();
    }
}