package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Need;

public interface NeedDAO {

    Need[] getNeeds() throws IOException;

    Need[] findNeeds(String containsText) throws IOException;

    Need getNeed(int id) throws IOException;

    Need createNeed(Need need) throws IOException;

    Need updateNeed(Need need) throws IOException;

    boolean deleteNeed(int id) throws IOException;
    
    Need contributeNeed(int id, double quantity);
}
