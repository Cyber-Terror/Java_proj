package com.defender.test.services;

import com.defender.test.model.Dorm;
import com.defender.test.model.Request;
import com.defender.test.model.User;
import com.defender.test.repositories.IDormRepository;
import com.defender.test.repositories.IRequestRepository;
import com.defender.test.services.serviceInterfaces.IMyRequestService;
import com.defender.test.services.serviceInterfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RequestService implements IMyRequestService {
    private final IRequestRepository requestRepository;
    private final IUserService userService;
    private final IDormRepository dormService;

    @Autowired
    public JavaMailSender emailSender;

    public RequestService(IRequestRepository requestRepository, IUserService userService, IDormRepository dormService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.dormService = dormService;
    }

    public List<Request> findAll() {
        var list = requestRepository.findAll();
        log.info("RequestService : find All Request");
        return list;
    }

    public void addRequest(String status, String message, User user, Dorm dorm) {
        Request request = new Request();
        request.setMessage(message);
        request.setStatus(status);
        request.setUser(user);
        request.setDorm(dorm);
        log.info("RequestService : add Request");
        this.requestRepository.save(request);
    }

    public Request findByUandC(User user, Dorm dorm) {
        return requestRepository.findAllByUserAndAndDorm(user, dorm).get(0);
    }

    public boolean isExist(User user, Dorm dorm) {
        return requestRepository.findAllByUserAndAndDorm(user, dorm).size() > 0;
    }

    public void setRequestStatus(String strToParse, boolean isAccept) {
        strToParse = strToParse.substring(1, strToParse.length() - 1);
        String[] list = strToParse.split(" => ");
        User user = userService.findByUsername(list[0]);
        Dorm dorm = dormService.findByName(list[1]);
        Request request = findByUandC(user, dorm);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Dormitary");

        if (isAccept) {
            request.setStatus("Accepted");
            message.setText("[ " + strToParse + " ]  " + "was accepted by admin");
        } else {
            request.setStatus("Refused");
            message.setText("[ " + strToParse + " ]  " + "was refused by admin");
        }
        if (user.getEmail() != null) {
            Runnable task = () -> {
                message.setTo(user.getEmail());
                this.emailSender.send(message);
            };
            task.run();
        }

        requestRepository.save(request);
    }
}
