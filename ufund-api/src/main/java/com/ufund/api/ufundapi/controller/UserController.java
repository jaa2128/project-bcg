package com.ufund.api.ufundapi.controller;

import org.apache.catalina.connector.Response;
import org.apache.coyote.http11.Http11AprProtocol;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
    public ResponseEntity<User> getUser(@PathVariable String username, @RequestParam String password) {
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
     * gets the ids of the needs in the user's basket
     * @param username the username of the user
     * @return NOT_FOUND if the user doesn't exist
     * @return the list of the ids if the user exists
     */
    @GetMapping("/{username}/needs")
    public ResponseEntity<ArrayList<Integer>> getNeeds(@PathVariable String username) {
        LOG.info("GET /users/" + username + "/needs");
        try {
            ArrayList<Integer> needs = userDao.getNeeds(username);
            if(needs == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else {
                return new ResponseEntity<ArrayList<Integer>>(needs, HttpStatus.OK);
            }
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * gets the quantities of the needs in the user's basket
     * @param username the username of the user
     * @return NOT_FOUND if the user doesn't exist
     * @return the list of the quantities if the user exists
     */
    @GetMapping("/{username}/contributions")
    public ResponseEntity<ArrayList<Integer>> getContributions(@PathVariable String username) {
        LOG.info("GET /users/" + username + "/contributions");
        try {
            ArrayList<Integer> contributions = userDao.getNeeds(username);
            if(contributions == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else {
                return new ResponseEntity<ArrayList<Integer>>(contributions, HttpStatus.OK);
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
    @PostMapping("/{username}")
    public ResponseEntity<User> createUser(@PathVariable String username, @RequestBody String password) {
        LOG.info("POST /users " + username);
        try {
            if (username.isEmpty() || password.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            User user = userDao.createUser(username, password);
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
     * @param needID the id of the need to add to the basket
     * @param quantity how much the user wants to contribute to the need
     * @return HttpStatus.BAD_REQUEST if the quantity is nonpositive
     * @return HttpStatus.NOT_FOUND if the user does not exist
     * @return HttpStatus.OK if the need was successfully added
     * @return HttpStatus.INTERNAL_SERVER_ERROR if there was an error
     */
    @PutMapping("/{username}/{needID}/{quantity}")
    public ResponseEntity<Integer> addNeed(@PathVariable String username, @PathVariable int needID, @PathVariable double quantity) {
        LOG.info("PUT /users " + username + "/" + needID);
        try {
            if(quantity <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Integer newNeedID = userDao.addNeed(username, needID, quantity);
            if(newNeedID == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else {
                return new ResponseEntity<Integer>(newNeedID, HttpStatus.OK);
            }
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Checks out the user's basket
     * @param username the username to remove the need from
     * @param id       the if of the need to remove
     * @return         HttpStatus.OK, if need is successfully removed
     * @return         HttpStatus.NOT_FOUND if the user doesn't exist
     * @return         HttpStatus.BAD_REQUEST if the need is not in the basket
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<Void> removeNeed(@PathVariable String username, @PathVariable int id) {
        LOG.info("DELETE " + id);
        try{
            Boolean needExists = userDao.removeNeed(username, id);
            if(needExists == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else if(!needExists) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
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
     * @return         HttpStatus.NOT_FOUND, if the user does not exist
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @DeleteMapping("/{username}/clear")
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