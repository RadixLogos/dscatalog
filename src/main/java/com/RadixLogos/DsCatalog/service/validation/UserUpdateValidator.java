package com.RadixLogos.DsCatalog.service.validation;

import com.RadixLogos.DsCatalog.dto.UserDTO;
import com.RadixLogos.DsCatalog.dto.errors.FieldMessage;
import com.RadixLogos.DsCatalog.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserDTO> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpServletRequest request;
    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {
        @SuppressWarnings("unchecked")
        var uriMap = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId =  Long.parseLong(uriMap.get("id"));

        List<FieldMessage> list = new ArrayList<>();
        var user = userRepository.findUserByEmail(dto.email());
        if(user != null && user.getId() != userId){
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
