package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

public interface UserDAO {

    User[] getUsers() throws IOException;
    
    User getUser(String username) throws IOException;

    User createUser(String username) throws IOException;

    Need addNeed(int id, double quantity) throws IOException;

    Need updateNeed(int id, double quantity) throws IOException;

    boolean removeNeed(int id) throws IOException;

    Need[] checkout() throws IOException;

    boolean clearBakser() throws IOException;


}
