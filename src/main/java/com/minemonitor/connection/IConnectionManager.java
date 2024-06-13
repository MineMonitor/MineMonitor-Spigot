package com.minemonitor.connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.minemonitor.connection.transfer.DataTransferObject;

import java.net.URI;
import java.net.URISyntaxException;

public interface IConnectionManager {

    void connect();
    void disconnect();
    boolean isConnected();
    boolean autoReconnect();
    void checkForReconnect();

    void sendData(String message);
    void sendData(DataTransferObject object) throws JsonProcessingException;

    void sendDataAsync(String message);

    void sendDataAsync(DataTransferObject object);


    URI getUri() throws URISyntaxException;

}

