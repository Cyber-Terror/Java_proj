package com.defender.test.rest;

import com.defender.test.Validator.PlayerValidator;
import com.defender.test.dto.AuthenticationRequestDto;
import com.defender.test.dto.RequestDto;
import com.defender.test.model.*;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RequestService requestService;
    private final DormService dormService;
    private final PlayerValidator playerValidator;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, RequestService requestService, DormService dormService, PlayerValidator playerValidator) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.requestService = requestService;
        this.dormService = dormService;
        this.playerValidator = playerValidator;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity  Register(@RequestBody AuthenticationRequestDto requestDto) throws MethodArgumentNotValidException {

        try {
            String username = requestDto.getUsername();
            User user = userService.findByUsername(username);
            if (user != null) {
                throw new UsernameNotFoundException("User with username: " + username + " already exist");
            }

            user = new User(requestDto.getUsername(), requestDto.getPassword());

            userService.register(user);

            Map<Object, Object> response = new HashMap<>();

            log.info("Get request : /api/v1/auth/registration");
            return new ResponseEntity<>(HttpStatus.OK, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            log.info("Get request : /api/v1/auth/registration ---- Invalid username");
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("token", token);
            log.info("Get request : /api/v1/auth/login");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            log.info("Get request : /api/v1/auth/login ---- Invalid username or password");
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @GetMapping(value = {"/userinfo"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity GetUsername(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user = userService.findByUsername(username);
        Role role = user.getRoles().get(0);
        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("role", role.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/champs"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dorm>> champList() {
        return new ResponseEntity<>(dormService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = {"/request"}, produces = MediaType.APPLICATION_JSON_VALUE)//pizdec
    public ResponseEntity<List<Request>> reqList() {
        return new ResponseEntity<>(requestService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = {"/dorms"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dorm>> dormList() {
        return new ResponseEntity<>(dormService.findAll()
                .stream()
                .filter(i -> i.getStatus() != Status.DELETED)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = {"/requests/{status}"})
    public ResponseEntity<List<RequestDto>> requestsList(@PathVariable(name = "status") String status) {
        var list = requestService.findAll()
                .stream()
                .filter(i -> i.getStatus().equals(status))
                .collect(Collectors.toList());
        List<RequestDto> ret_list = new ArrayList<>();
        for (int i = 0; i < 10 && i < list.size(); i++) {
            ret_list.add(RequestDto.FromRequest(list.get(i)));
        }
        return new ResponseEntity<>(ret_list, HttpStatus.OK);
    }

    @PostMapping(value = {"/request"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestDto> getRequestsMessage(@RequestBody String string) {
        string = string.substring(1, string.length() - 1);
        String[] list = string.split(" => ");
        User user = userService.findByUsername(list[0]);
        Dorm dorm = dormService.findByName(list[1]);
        Request request = requestService.findByUandC(user, dorm);
        var ret = RequestDto.FromRequest(request);
        return new ResponseEntity<RequestDto>(ret, HttpStatus.OK);
    }

}
