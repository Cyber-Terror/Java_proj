package com.defender.test.services;

import com.defender.test.model.Dorm;
import com.defender.test.model.Status;
import com.defender.test.repositories.IDormRepository;
import com.defender.test.services.serviceInterfaces.IDormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DormService implements IDormService {
    private final IDormRepository dormRepository;

    @Autowired
    public DormService(IDormRepository dormRepository) {
        this.dormRepository = dormRepository;
    }

    @Override
    public List<Dorm> findAll() {
        log.info("DormService : findAll");
        return this.dormRepository.findAll();
    }

    @Override
    public void deleteDormByName(String name) {
        Dorm deDorm = this.dormRepository.findAllByName(name).get(0);
        deDorm.setStatus(Status.DELETED);
        this.dormRepository.save(deDorm);
        log.info("DormService : deleteSubjectByName");
    }

    public Dorm findByName(String name){
        return dormRepository.findByName(name);
    }

    @Override
    public void addDorm(String name) {
        Dorm dorm1 = new Dorm();
        dorm1.setStatus(Status.ACTIVE);
        dorm1.setCreated(new Date());
        dorm1.setUpdated(new Date());
        dorm1.setName(name);
        this.dormRepository.save(dorm1);
        log.info("DormService : addSubject");
    }

}
