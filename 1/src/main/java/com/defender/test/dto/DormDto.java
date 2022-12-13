package com.defender.test.dto;


import com.defender.test.model.Dorm;
import com.defender.test.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DormDto {
    private List<Dorm> dorms;

    public static Dorm toSubject(DormDto dormDto) {
        Dorm dorm = new Dorm();
        dorm.setName(dormDto.dorms.get(0).getName());
        return dorm;
    }

    public static DormDto fromUser(User user) {
        DormDto dormDto = new DormDto();
        return dormDto;
    }

    public void setSubject(List<Dorm> dorm) {
        this.dorms = dorm;
    }
}