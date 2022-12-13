package com.defender.test.repositories;

import com.defender.test.model.Dorm;
import com.defender.test.model.Request;
import com.defender.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRequestRepository  extends JpaRepository<Request, Integer>  {
    Request findByUserAndAndDorm(User user, Dorm dorm);
    List<Request> findAllByUserAndAndDorm(User user, Dorm dorm);
    <S extends User> S save(S s);
}
