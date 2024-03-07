package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

public interface UserDAO {

    User[] getUsers() throws IOException;
    
    User getUser(String username) throws IOException;

    User createUser(String username) throws IOException;

    Need addNeed(String username, Need need, double quantity) throws IOException;
    
    boolean removeNeed(String username, Need need) throws IOException;

    Need[] checkout(String username) throws IOException;

    boolean clearBasket(String username) throws IOException;


}
