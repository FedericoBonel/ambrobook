package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.UserDto;
import com.fedebonel.recipemvc.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDto implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {
        UserDto userDto = new UserDto();

        userDto.setId(source.getId());
        userDto.setActive(source.getActive());
        userDto.setUsername(source.getUsername());
        userDto.setEmail(source.getEmail());
        userDto.setPassword(source.getPassword());
        userDto.setUserRoles(source.getUserRoles());

        return userDto;
    }
}
