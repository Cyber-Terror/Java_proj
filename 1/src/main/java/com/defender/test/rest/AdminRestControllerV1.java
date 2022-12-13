package com.defender.test.rest;

import com.defender.test.dto.AdminUserDto;
import com.defender.test.model.Dorm;
import com.defender.test.model.User;
import com.defender.test.services.DormService;
import com.defender.test.services.RequestService;
import com.defender.test.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/admin/")
public class AdminRestControllerV1 {

    private final UserService userService;
    private final DormService subjectService;
    private final RequestService requestService;

    @Autowired
    public AdminRestControllerV1(UserService userService, DormService subjectService, RequestService requestService) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.requestService = requestService;
    }

    @GetMapping(value = "users/{username}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "username") String username) {
        User user;
        user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        AdminUserDto result = AdminUserDto.fromUser(user);
        log.info("Get request : /api/v1/admin/users");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("addDorm")
    public ResponseEntity addDorm(RequestEntity<Dorm> dorm) {
        subjectService.addDorm(Objects.requireNonNull(dorm.getBody()).getName());
        log.info("Get request : /api/v1/admin/addDorm");
        return new ResponseEntity<>(Objects.requireNonNull(dorm.getBody()).getName(), HttpStatus.CREATED);
    }

    @PostMapping(value = {"/acceptRequest"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity acceptRequest(@RequestBody String string) {
        requestService.setRequestStatus(string,true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = {"/refuseRequest"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity refuseRequest(@RequestBody String string) {
        requestService.setRequestStatus(string,false);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("deActivateDorm")
    public ResponseEntity deActivateDorm(RequestEntity<Dorm> dorm) {
        subjectService.deleteDormByName(Objects.requireNonNull(dorm.getBody()).getName());
        log.info("Get request : /api/v1/admin/deActivateDorm");
        return new ResponseEntity<>(Objects.requireNonNull(dorm.getBody()).getName(), HttpStatus.CREATED);
    }
}
