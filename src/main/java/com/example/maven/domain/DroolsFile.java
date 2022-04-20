package com.example.maven.domain;

import java.util.Date;
import java.util.List;


public class DroolsFile {
    private Long id;
    private String globalContent;

    private Date createdDate;
    private Date updatedDate;
    private List<Rule> rules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGlobalContent() {
        return globalContent;
    }

    public void setGlobalContent(String globalContent) {
        this.globalContent = globalContent;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "DroolsFile{" +
                "id=" + id +
                ", globalContent='" + globalContent + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", rules=" + rules +
                '}';
    }
}
