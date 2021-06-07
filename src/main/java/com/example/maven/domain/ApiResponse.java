package com.example.maven.domain;

import java.util.List;


public class ApiResponse {
    private String requestType;
    private String url;
    private Integer score;
    private List<String> messages;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "requestType='" + requestType + '\'' +
                ", url='" + url + '\'' +
                ", score=" + score +
                ", messages=" + messages +
                '}';
    }
}
