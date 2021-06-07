package com.example.maven.domain;

import java.util.ArrayList;
import java.util.List;


public class APIDto {
    private String url;
    private String type;
    private List<String> responseCodes;
    private List<String> securities;
    private List<String> messages = new ArrayList<>();
    private Integer score = 0;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getResponseCodes() {
        return responseCodes;
    }

    public void setResponseCodes(List<String> responseCodes) {
        this.responseCodes = responseCodes;
    }

    public List<String> getSecurities() {
        return securities;
    }

    public void setSecurities(List<String> securities) {
        this.securities = securities;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
