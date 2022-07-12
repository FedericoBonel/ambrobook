package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.UserDto;
import com.fedebonel.recipemvc.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUser implements Converter<UserDto, User> {

    @Nullable
    @Override
    public User convert(UserDto source) {
        User user = new User();

        user.setId(source.getId());
        user.setActive(source.getActive());
        user.setUsername(source.getUsername());
        user.setEmail(source.getEmail());
        user.setPassword(source.getPassword());
        user.setUserRoles(source.getUserRoles());

        return user;
    }
}
