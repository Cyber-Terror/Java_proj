package com.defender.test.services.serviceInterfaces;

import com.defender.test.model.User;

import java.util.List;

public interface IUserService {
    User register(User user);
    List<User> getAll();
    User findByUsername(String userName);
    User findByEmail(String userName);
    User findById(Long id);
    void delete(Long id);
}
