package com.defender.test.dto;

import com.defender.test.model.Request;
import com.defender.test.services.DormService;
import com.defender.test.services.RequestService;
import com.defender.test.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class ParseStringFromFrontDto {
    private String string;

    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;
    @Autowired
    private DormService dormService;

    ParseStringFromFrontDto(){
    }

    public ParseStringFromFrontDto(String string) {
        this.string = string;
    }

    public RequestDto GetRequestFromString(){
        string = string.substring(1,string.length()-1);
        String[] list = string.split("=>");
        Request request =
                requestService.findByUandC(
                        userService.findByUsername(list[0]),
                        dormService.findByName(list[1])
                );
        return RequestDto.FromRequest(request);
    }
}
