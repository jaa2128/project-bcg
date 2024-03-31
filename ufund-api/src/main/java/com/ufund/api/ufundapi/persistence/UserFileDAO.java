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
import com.fasterxml.jackson.databind.module.SimpleModule;

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

        // //JSON files cannot naturally use objects as map keys, so we need to register a key
        // //deserializer so that the ObjectMapper knows how to deserialize Needs
        // SimpleModule module = new SimpleModule();
        // module.addKeyDeserializer(Need.class, new NeedKeyDeserializer());
        // this.objectMapper.registerModule(module);

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
     * @return the User with the given username
     * @return null if no such user exists with the given username
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
     * gets the ids of the needs in the user's basket
     * @param username the username of the user
     * @return null if the user doesn't exist
     * @return the list of the need ids if the user exists
     */
    @Override
    public ArrayList<Integer> getNeeds(String username) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null) {
                return null;
            }
            return user.getNeeds();
        }
    }

    /**
     * gets the quantities of the needs in the user's basket
     * @param username the username of the user
     * @return null if the user doesn't exist
     * @return the list of the quantities if the user exists
     */
    @Override
    public ArrayList<Double> getContributions(String username) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null) {
                return null;
            }
            return user.getContributions();
        }
    }

    /**
     * Creates a new user
     * @param username the username of the new user
     * @return the new user if the given username was not already in use
     * @return null if the username was alrady in use
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
     * @return the if of the need if it was successfully added
     * @return null if the user doesn't exist
     */
    @Override
    public Integer addNeed(String username, int id, double quantity) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null) {
                return null;
            }
            user.addNeed(id, quantity);
            save(); //may throw IOException
            return id;
        }
    }

    /**
     * edits a contribution in a user's basket
     * @param username the username of the user
     * @param id the id of the need to be edited
     * @param quantity the new quantity to be associated with the need
     * @return null if the user does not exist
     * @return -1 if the need does not exist
     * @return the id of the need if it was successfully updated 
     */
    @Override
    public Integer editNeed(String username, int id, double quantity) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null) {
                return null;
            }
            int edited = user.editNeed(id, quantity);
            save(); //may throw IOException
            return edited;
        }
    }

    /**
     * Removes a need from the user basket
     * @param username the username of the user
     * @param need the need to remove from the user's basket
     * 
     * @return null if the user doesn't exist
     * @return false if the need doesn't exist in the basket
     * @return true if the need was successfully removed
     */
    @Override
    public Boolean removeNeed(String username, int needID) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null) {
                return null;
            }
            boolean isRemoved = user.removeNeed(needID);
            save(); //may throw IOException
            return isRemoved;
        }
    }

    /**
     * Clears the user basket
     * @param username the username of the user
     * 
     * @return false if the user doesn't exist
     * @return true if the basket was cleared
     */
    @Override
    public boolean clearBasket(String username) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null) {
                return false;
            }
            user.clearBasket();
            save(); //may throw IOException
            return true;
        }
    }

    /**
     * Clears the user basket
     * @param username the username of the user
     * @param newUsername the username to change to
     * 
     * @return false if the user doesn't exist
     * @return true if the basket was cleared
     */
    public boolean changeUsername(String username, String newUsername) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null) {
                return false;
            }
            if(newUsername != null && getUser(newUsername) == null) {
                user.changeUsername(newUsername);
                save();
                return true;
            }
            return false;
        }
    }

    /**
     * Clears the user basket
     * @param username the username of the user
     * @param newPassword the new password to change to
     * 
     * @return false if the user doesn't exist
     * @return true if the basket was cleared
     */
    public boolean changePassword(String username, String newPassword) throws IOException {
        synchronized(users) {
            User user = getUser(username);
            if(user == null || newPassword == null) {
                return false;
            }
            user.changePassword(newPassword);
            save();
            return true;
        }
    }
}