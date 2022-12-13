package com.defender.test.services.serviceInterfaces;

import com.defender.test.model.Dorm;

import java.util.List;

public interface IDormService {
    List<Dorm> findAll();
    void deleteDormByName(String name);
    void addDorm(String name);
}