package com.defender.test.repositories;

import com.defender.test.model.Dorm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDormRepository extends JpaRepository<Dorm, Integer> {
    List<Dorm> findAllByName(String name);
    Dorm findByName(String name);
    <S extends Dorm> S save(S s);
}
