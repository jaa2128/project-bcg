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

    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        return null;
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(String username) {
        return null;
    }

    @PutMapping("/{username}")
    public ResponseEntity<Need> addNeed(@PathVariable String username, Need need, double quantity) {
        return null;
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Need> removeNeed(@PathVariable String username, Need need, double quantity) {
        return null;
    }

    @PutMapping("/{username}")
    public ResponseEntity<Need[]> checkout(@PathVariable String username) {
        return null;
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