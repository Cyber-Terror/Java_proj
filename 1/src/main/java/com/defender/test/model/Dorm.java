package com.defender.test.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "dorms")
@Setter
@Getter
public class Dorm extends BaseEntity{
    public Dorm() {
    }

    public Dorm(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String subject) {
        this.name = subject;
    }
}
