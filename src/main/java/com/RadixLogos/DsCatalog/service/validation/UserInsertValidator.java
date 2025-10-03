package com.RadixLogos.DsCatalog.service.validation;

import com.RadixLogos.DsCatalog.dto.UserInsertDTO;
import com.RadixLogos.DsCatalog.dto.errors.FieldMessage;
import com.RadixLogos.DsCatalog.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();
        var user = userRepository.findUserByEmail(dto.userDTO().email());
        if(user != null){
            list.add(new FieldMessage("email","Email já existe!"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.message()).addPropertyNode(e.field())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
