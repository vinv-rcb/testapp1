package com.nhalamphitrentroi.testapp1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuggestionDoneRequest {
    @JsonProperty("id")
    private String id;
    
    // Default constructor
    public SuggestionDoneRequest() {}
    
    // Constructor with id
    public SuggestionDoneRequest(String id) {
        this.id = id;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "SuggestionDoneRequest{" +
                "id='" + id + '\'' +
                '}';
    }
}
