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

    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need) {
        LOG.info("PUT /needs " + need);
        try {
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

    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        LOG.info("POST /needs " + need);
        try {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id){
        LOG.info("DELETE /heroes/" + id);

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
}

// PUT MORE CODE HERE //