package com.minemonitor.connection.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class DataTransferObject {


    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static <U> U fromJson(String value, Class<U> dataTransferObject) throws JsonProcessingException {
        return new ObjectMapper().readValue(value, dataTransferObject);
    }
}
