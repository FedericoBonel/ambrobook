package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.UserDto;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.mappers.UserDtoToUser;
import com.fedebonel.recipemvc.mappers.UserToUserDto;
import com.fedebonel.recipemvc.model.User;
import com.fedebonel.recipemvc.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoToUser toUser;
    private final UserToUserDto toUserDto;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UserServiceImpl(UserRepository userRepository,
                           UserDtoToUser toUser,
                           UserToUserDto toUserDto,
                           PasswordEncoder passwordEncoder,
                           MailService mailService) {
        this.userRepository = userRepository;
        this.toUser = toUser;
        this.toUserDto = toUserDto;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto, String serverUrl) {
        User user = toUser.convert(userDto);
        user.setUsername(user.getUsername().replaceAll("\\s", ""));
        user.getUserRoles().forEach(role -> role.setUser(user));
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationCode(RandomString.make(64));

        try {
            mailService.sendVerificationEmail(user, serverUrl);
        } catch (Exception e) {
            log.debug("There was a problem sending the email: " + e.getMessage());
        }

        return toUserDto.convert(userRepository.save(user));
    }

    @Override
    public UserDto findByUsername(String username) {
        if (username == null) return null;

        return userRepository.findByUsername(username)
                .map(toUserDto::convert)
                .orElse(null);
    }

    @Override
    public UserDto findByEmail(String email) {
        if (email == null)  return null;

        return userRepository.findByEmail(email)
                .map(toUserDto::convert)
                .orElse(null);
    }

    @Override
    public UserDto findByVerification(String verificationCode) {
        return userRepository.findByVerificationCode(verificationCode)
                .map(toUserDto::convert)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void verify(UserDto userDto) {
        userDto.setActive(true);

        userRepository.save(toUser.convert(userDto));
    }
}
