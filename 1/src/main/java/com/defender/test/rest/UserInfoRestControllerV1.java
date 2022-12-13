package com.defender.test.rest;

import com.defender.test.Validator.PlayerValidator;
import com.defender.test.dto.AdminUserDto;
import com.defender.test.dto.PlayerDto;
import com.defender.test.model.Role;
import com.defender.test.model.User;
import com.defender.test.security.jwt.JwtTokenProvider;
import com.defender.test.services.DormService;
import com.defender.test.services.RequestService;
import com.defender.test.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/userinfo")
public class UserInfoRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RequestService requestService;
    private final DormService dormService;
    private final PlayerValidator playerValidator;

    @Autowired
    public UserInfoRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, RequestService requestService, DormService dormService, PlayerValidator playerValidator) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.requestService = requestService;
        this.dormService = dormService;
        this.playerValidator = playerValidator;
    }

    @GetMapping(value = {"/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity GetUsername(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user = userService.findByUsername(username);
        Role role = user.getRoles().get(0);
        if(role.getName().equals("ROLE_PLAYER")) {
            PlayerDto playerDto = PlayerDto.fromUser(user);
            log.info("Get request : /api/v1/userinfo/ -- PLAYER");
            return new ResponseEntity<>(playerDto, HttpStatus.OK);
        }else{
            AdminUserDto adminUserDto = AdminUserDto.fromUser(user);
            log.info("Get request : /api/v1/userinfo/ -- ADMIN");
            return new ResponseEntity<>(adminUserDto, HttpStatus.OK);
        }
    }
}
