package com.defender.test.dto;

import com.defender.test.model.Request;
import lombok.Data;

@Data
public class RequestDto {
    private String status;
    private String message;
    private String user;
    private String dorm;

    public RequestDto(String status, String message, String user, String dorm) {
        this.status = status;
        this.message = message;
        this.user = user;
        this.dorm = dorm;
    }

    public RequestDto(){

    }

    public static RequestDto FromRequest(Request request) {
        RequestDto req = new RequestDto();
        req.status = request.getStatus();
        req.message = request.getMessage();
        req.user = request.getUser().getUsername();
        req.dorm = request.getDorm().getName();
        return req;
    }
}
