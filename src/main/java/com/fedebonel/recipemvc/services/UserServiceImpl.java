package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.UserDto;
import com.fedebonel.recipemvc.mappers.UserDtoToUser;
import com.fedebonel.recipemvc.mappers.UserToUserDto;
import com.fedebonel.recipemvc.model.User;
import com.fedebonel.recipemvc.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoToUser toUser;
    private final UserToUserDto toUserDto;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           UserDtoToUser toUser,
                           UserToUserDto toUserDto,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.toUser = toUser;
        this.toUserDto = toUserDto;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = toUser.convert(userDto);
        user.getUserRoles().forEach(role -> role.setUser(user));
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return toUserDto.convert(userRepository.save(user));
    }
}
