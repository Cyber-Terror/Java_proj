package com.defender.test.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Request {
    @Id
    @GeneratedValue
    private int id;
    private String message;
    private String status;

    @ManyToOne
    private User user;
    @ManyToOne
    private Dorm dorm;

    public Request(){
    }

    public Request(String message, String status, User user, Dorm dorm) {
        this.message = message;
        this.status = status;
        this.user = user;
        this.dorm = dorm;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Dorm getDorm() {
        return dorm;
    }

    public void setDorm(Dorm dorm) {
        this.dorm = dorm;
    }
}
