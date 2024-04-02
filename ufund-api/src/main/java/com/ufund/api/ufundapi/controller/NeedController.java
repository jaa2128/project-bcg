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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.persistence.NeedFileDAO;
import com.ufund.api.ufundapi.model.Need;

@RestController
@RequestMapping("needs")
public class NeedController {
    
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedDAO needDao;

    public NeedController(NeedDAO needDao) {
        this.needDao = needDao;
    }



    /**
     * Updates a single need object.
     * @param need The need object to update.
     * @return HTTP status code depending on success: 200 if succesfull, 404 if client error, 500 if server error
     */
    @PutMapping("")
    public ResponseEntity updateNeed(@RequestBody Need need) {
        LOG.info("PUT /needs " + need);
        try {
            if(need.getName().isEmpty()){
                return new ResponseEntity<String>("Name Field Empty", HttpStatus.NOT_ACCEPTABLE);
            }

            if(need.getDescription().isEmpty()){
                return new ResponseEntity<String>("Description Field Empty", HttpStatus.NOT_ACCEPTABLE);
            }

            if(need.getType().isEmpty()){
                return new ResponseEntity<String>("Type Field Empty", HttpStatus.NOT_ACCEPTABLE);
            }

            if(need.getTargetQuantity() <= 0){
                return new ResponseEntity<String>("Goal Amount Field Empty", HttpStatus.NOT_ACCEPTABLE);
            }

            for (Need otherNeed : needDao.getNeeds()){
                if(need.getName().equals(otherNeed.getName()) && need.getID() != otherNeed.getID()){ return new ResponseEntity<>(HttpStatus.CONFLICT); }
            }

            need = needDao.updateNeed(need);
            if(need != null)
                return new ResponseEntity<Need>(need,HttpStatus.OK);
            else
                return new ResponseEntity<Need>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Takes the JSON Body and converts it into a Need object
     * Passes need into createNeed() function in needDao interface
     * to create newNeed
     * @param need     Need object created from JSON Body
     * @return         newNeed and HttpStatus.CREATED, if newNeed is not null
     * @return         HttpStatus.CONFLICT, if newNeed is null
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        LOG.info("POST /needs " + need);
        try {
            if (need.hasEmptyField()){
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            Need newNeed = needDao.createNeed(need);
            if(newNeed!=null){
                return new ResponseEntity<Need>(newNeed, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

/**
     * Retrieves all the current Needs stored
     * @return an array of all the Needs stored with status code 200 if there is no server error
     * @return error code 500 if there is an internal server error
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds() {
        LOG.info("GET /needs");
        try {
            Need[] needs = needDao.getNeeds();
            return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Takes a Need id and gets a need from the JSON Body
     * @param id       id of the Need object retrieved from JSON Body
     * @return         need and HttpStatus.OK, if need is successfully retrieved
     * @return         HttpStatus.NOT_FOUND, if need is null
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id){
        LOG.info("GET /needs/" + id);
        try{
            Need need = needDao.getNeed(id);
            if(need != null)
                return new ResponseEntity<Need>(need, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Takes a Need id and deletes it from the JSON Body
     * @param id       id of the Need object that is going to be deleted from JSON Body
     * @return         HttpStatus.OK, if need is successfully deleted
     * @return         HttpStatus.NOT_FOUND, if need doesn't exists
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id){
        LOG.info("DELETE /needs/" + id);

        try{
            boolean needExists = needDao.deleteNeed(id);
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
     * Takes a Need id and deletes it from the JSON Body
     * @param name     The search term to be used
     * @return         HttpStatus.OK, if need is successfully deleted
     * @return         HttpStatus.INTERNAL_SERVER_ERROR, if exception is caught
     */
    @GetMapping("/")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String name, @RequestParam String type, @RequestParam String min, @RequestParam String max) {

        LOG.info("GET /needs/?name="+name+"&type="+type+"&min="+min+"&max="+max);
        try {
            Need[] searchList = needDao.findNeeds(name, type, min, max);
            return new ResponseEntity<Need[]>(searchList,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Contributes to a need by a certain amount
     * @param id the ID of the need to contribute to
     * @param quantity the amount to be contributed
     * @return HttpStatus.BAD_REQUEST if quantity is not positive
     * @return HttpStatus.NOT_FOUND if the given id does not exist
     * @return HttpStatus.OK if the need was successfully updated
     * @return HttpStatus.INTERNAL_SERVER_ERROR if exception is caught
     */
    @PutMapping("/{id}")
    public ResponseEntity<Need> contributeNeed(@PathVariable int id, @RequestBody double quantity) {
        LOG.info("PUT /needs/" + id);
        try {
            //return BAD_REQUEST if we try to contribute a nonpositive value
            if(quantity <= 0.0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            //call contributeNeed to update currentQuantity
            Need need = needDao.contributeNeed(id, quantity);
            //needDao.contributeNeed() returns null if need was not found
            if(need == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            //otherwise, change was successful
            else {
                return new ResponseEntity<Need>(need, HttpStatus.OK);
            }
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

// PUT MORE CODE HERE //