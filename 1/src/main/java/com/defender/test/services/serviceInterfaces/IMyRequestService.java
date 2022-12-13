package com.defender.test.services.serviceInterfaces;

import com.defender.test.model.Dorm;
import com.defender.test.model.User;

public interface IMyRequestService {
    void addRequest(String status, String message, User user, Dorm dorm);
}
