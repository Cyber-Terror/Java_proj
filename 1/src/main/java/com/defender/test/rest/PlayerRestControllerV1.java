package com.defender.test.rest;

import com.defender.test.dto.PlayerDto;
import com.defender.test.model.Dorm;
import com.defender.test.model.User;
import com.defender.test.services.DormService;
import com.defender.test.services.RequestService;
import com.defender.test.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/players/")
public class PlayerRestControllerV1 {
    private final UserService userService;
    private final DormService dormService;
    private final RequestService requestService;

    @Autowired
    public PlayerRestControllerV1(UserService userService, DormService dormService, RequestService requestService) {
        this.userService = userService;
        this.dormService = dormService;
        this.requestService = requestService;
    }

    @GetMapping(value = "{username}")
    public ResponseEntity<PlayerDto> getPlayerByUsername(@PathVariable(name = "username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PlayerDto result = PlayerDto.fromUser(user);
        log.info("Get request : /api/v1/auth/username");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "makeRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity makeRequests(@RequestBody String string) {
        log.info("Player make request");
        string = string.substring(1, string.length() - 1);
        String[] list = string.split("]");
        User user = userService.findByUsername(list[1]);
        Dorm dorm = dormService.findByName(list[0]);
        if (!requestService.isExist(user, dorm)) {
            requestService.addRequest(
                    "Pending",
                    list[2],
                    user,
                    dorm
            );
            return new ResponseEntity(HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "changeEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeEmail(@RequestBody String string) {
        log.info("Player change email");
        string = string.substring(1, string.length() - 1);
        String[] list = string.split("]");
        User user = userService.findByUsername(list[0]);

        user.setEmail(list[1]);

        userService.save(user);

        return new ResponseEntity(HttpStatus.CONFLICT);
    }
}
