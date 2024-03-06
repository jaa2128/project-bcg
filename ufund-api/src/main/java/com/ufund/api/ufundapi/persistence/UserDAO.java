package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

public interface UserDAO {

    User[] getUsers() throws IOException;
    
    User getUser(String username) throws IOException;

    User createUser(String username) throws IOException;

    Need addNeed(String username, int id, double quantity) throws IOException;

    Need updateNeed(String username, int id, double quantity) throws IOException;

    boolean removeNeed(String username, int id) throws IOException;

    Need[] checkout(String username) throws IOException;

    boolean clearBakset(String username) throws IOException;


}
