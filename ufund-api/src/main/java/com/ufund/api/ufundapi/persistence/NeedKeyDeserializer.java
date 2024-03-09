package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ufund.api.ufundapi.model.Need;

import java.io.IOException;

public class NeedKeyDeserializer extends KeyDeserializer {

    /**
     * takes a Need key from users.json and deserializes it into a Need object
     * this is the only file where the 6-param Need constructor should be used
     */
    @Override
    public Need deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(key);

        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        String description = node.get("description").asText();
        String type = node.get("type").asText();
        double targetQuantity = node.get("targetQuantity").asDouble();
        double currentQuantity = node.get("currentQuantity").asDouble();

        return new Need(id, name, description, type, targetQuantity, currentQuantity);
    }
}
