package com.defender.test.Validator;


import com.defender.test.forms.RegistrationPlayerModel;
import com.defender.test.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PlayerValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return RegistrationPlayerModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegistrationPlayerModel user = (RegistrationPlayerModel) o;

        if(userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "", "This username is already in use");
        }

        if(userService.findByEmail(user.getEmail()) != null){
            errors.rejectValue("email", "", "This Email is already in use");
        }

    }
}
