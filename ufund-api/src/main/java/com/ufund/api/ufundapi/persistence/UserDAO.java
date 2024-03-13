package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

public interface UserDAO {

    User[] getUsers() throws IOException;
    
    User getUser(String username) throws IOException;

    boolean authenticateUser(String username, String password) throws IOException;

    User createUser(String username, String password) throws IOException;

    Need addNeed(String username, Need need, double quantity) throws IOException;
    
    Boolean removeNeed(String username, int needID) throws IOException;

    boolean checkout(String username) throws IOException;

    boolean clearBasket(String username) throws IOException;


}
