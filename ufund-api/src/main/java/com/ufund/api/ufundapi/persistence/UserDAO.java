package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.ArrayList;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

public interface UserDAO {

    User[] getUsers() throws IOException;
    
    User getUser(String username) throws IOException;

    ArrayList<Integer> getNeeds(String username) throws IOException;

    ArrayList<Double> getContributions(String username) throws IOException;

    boolean authenticateUser(String username, String password) throws IOException;

    User createUser(String username, String password) throws IOException;

    Integer addNeed(String username, int id, double quantity) throws IOException;
    
    Boolean removeNeed(String username, int needID) throws IOException;

    boolean clearBasket(String username) throws IOException;
}
