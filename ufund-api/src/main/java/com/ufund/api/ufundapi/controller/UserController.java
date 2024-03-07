package com.ufund.api.ufundapi.controller;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.persistence.UserFileDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

@RestController
@RequestMapping("users")
public class UserController {
    
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDao;

    public UserController(UserDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * gets an array of all the users in the JSON
     * @return an array of all the users with HttpStatus.OK if there were no errors
     * @return HttpStatus.INTERNAL_SERVER_ERROR if ther was an error
     */
    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users/");
        try {
            User[] users = userDao.getUsers();
            return new ResponseEntity<User[]>(users, HttpStatus.OK);
        } catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a specific user from the JSON file
     * @param username the username of the user
     * @return the user object with HttpStatus.OK if the user exists and the password is correct
     * @return HttpStatus.NOT_FOUND if the user does not exist
     * @return HttpStatus.UNAUTHORIZED if the password is incorrect
     * @return HttpStatus.INTERNAL_SERVER_ERROR if there was an error
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username, String password) {
        LOG.info("GET /users/" + username);
        try {
            User user = userDao.getUser(username);
            if(user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else {
                if(userDao.authenticateUser(username, password)) {
                    return new ResponseEntity<User>(user, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * creates a new user in the JSON
     * @param username the username of the new user
     * @return HttpStatus.BAD_REQUEST if the username is blank
     * @return HttpStatus.CREATED and the new user if the user was successfully added
     * @return HttpStatus.CONFLICT if the username was already in use
     * @return HttpStatus.INTERNAL_SERVER_ERROR if there was an I/O error
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(String username) {
        LOG.info("POST /users " + username);
        try {
            if (username.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            User user = userDao.createUser(username);
            if(user != null){
                return new ResponseEntity<User>(user, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * adds a need to a user's basket in the JSON
     * @param username the username of the user
     * @param need the need to add to the basket
     * @param quantity how much the user wants to contribute to the need
     * @return HttpStatus.NOT_FOUND if the user does not exist
     * @return HttpStatus.OK if the need was successfully added
     * @return HttpStatus.INTERNAL_SERVER_ERROR if there was an error
     */
    @PutMapping("/{username}")
    public ResponseEntity<Need> addNeed(@PathVariable String username, Need need, double quantity) {
        LOG.info("PUT /users " + username + "/" + need);
        try {
            Need newNeed = userDao.addNeed(username, need, quantity);
            if(newNeed == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else {
                return new ResponseEntity<Need>(newNeed, HttpStatus.OK);
            }
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Checks out the user's basket
     * @param username the username to remove the need from
     * @param need the need to remove
     * @return         HttpStatus.OK, if basket is successfully cleared
     * @return         HttpStatus.NOT_FOUND, if the need was not removed or does not exist
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Need> removeNeed(@PathVariable String username, Need need) {
        LOG.info("DELETE " + need.getID());
        try{
            boolean needExists = userDao.removeNeed(username, need);
            if(needExists)
                return new ResponseEntity<>(HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Checks out the user's basket
     * @param username the username to checkout the basket of
     * @return         HttpStatus.OK, if basket is successfully cleared
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Need[]> checkout(@PathVariable String username) {
        LOG.info("CHECKOUT " + username);
        try{
            Need[] checkoutNeeds = userDao.checkout(username);
            return new ResponseEntity<>(checkoutNeeds, HttpStatus.OK);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }   
    
    /**
     * Clears the user's basket
     * @param username the username to clear the basket of
     * @return         HttpStatus.OK, if basket is successfully cleared
     * @return         HttpStatus.NOT_FOUND, if there are errors clearing the basket
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Boolean> clearBasket(@PathVariable String username) {
        LOG.info("CLEAR BASKET " + username);
        try{
            boolean basketClear = userDao.clearBasket(username);
            if(basketClear)
                return new ResponseEntity<>(HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}