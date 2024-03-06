package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
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
     * Retrieves all current needs stored in the JSOn
     * @return all the needs stored in an array
     */
    @Override
    public User[] getUsers() throws IOException {
        synchronized(users){
            return getUsersArray();
        }
    }


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

    @Override
    public User createUser(String username) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public Need addNeed(String username, int id, double quantity) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNeed'");
    }

    @Override
    public Need updateNeed(String username, int id, double quantity) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNeed'");
    }

    @Override
    public boolean removeNeed(String username, int id) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeNeed'");
    }

    @Override
    public Need[] checkout(String username) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkout'");
    }

    @Override
    public boolean clearBakset(String username) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearBakser'");
    }
}